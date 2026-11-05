'use client'

import { Produto } from "@/app/types/produto";
import { buscarListaProdutos, deletarProduto } from "@/app/services/produtoService";
import Link from "next/link";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "@/app/redux/store";
import { definirBusca, definirStatus } from "@/app/redux/slices/produtoFiltroSlice";

export default function Produtos() {

    const [produtos, setProdutos] = useState<Produto[]>([])

    // enquanto a lista não chega do back, não faz sentido mostrar "Nenhum produto encontrado"
    const [carregando, setCarregando] = useState(true)

    // busca e filtro de status ficam na store do Redux (produtoFiltroSlice)
    const busca = useSelector((state: RootState) => state.produtoFiltro.busca);
    const filtroStatus = useSelector((state: RootState) => state.produtoFiltro.status);

    const dispatch = useDispatch();

    useEffect(()=>{
        carregarDados();
    },[]);

    const carregarDados = async ()=>{

        try {
            const dados = await buscarListaProdutos();

            setProdutos(dados);
        } catch (error) {
            alert("Erro ao carregar dados")
        } finally {
            setCarregando(false)
        }

    }

    const handlerDeletarProduto = async(produto: Produto)=> {

        if (!confirm(`Deseja realmente excluir o produto "${produto.nome}"?`)) {
            return;
        }

        try {
            await deletarProduto(produto.id || 0)
            alert("Produto foi excluído com sucesso!");
        } catch (error) {
            // no back só o ADMIN pode excluir produtos (DELETE em /produtos devolve 403 para os outros perfis)
            const status = (error as { response?: { status?: number } }).response?.status;

            alert(status === 403 ? "Você não tem permissão para excluir produtos!" : "Erro ao excluir produto");
            return;
        }

        carregarDados();
    }

    const formatarMoeda = (valor: number) => valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });

    // usa só "aaaa-mm-dd" para o fuso horário não recuar o dia (ex.: 00:00 UTC vira o dia anterior no Brasil)
    const formatarData = (data: string) => new Date(data.slice(0, 10) + "T00:00:00").toLocaleDateString("pt-BR");

    // dias entre hoje e o vencimento (negativo quando já passou)
    const diasParaVencer = (data: string) => {
        const hoje = new Date();
        hoje.setHours(0, 0, 0, 0);

        const vencimento = new Date(data.slice(0, 10) + "T00:00:00");

        return Math.round((vencimento.getTime() - hoje.getTime()) / 86400000);
    };

    const descreverVencimento = (data: string) => {
        const dias = diasParaVencer(data);

        if (dias === 0) return "Vence hoje";
        if (dias > 0) return `Vence em ${dias} ${dias === 1 ? "dia" : "dias"}`;
        return `Venceu há ${Math.abs(dias)} ${Math.abs(dias) === 1 ? "dia" : "dias"}`;
    };

    const statusLabels: Record<string, string> = {
        NA_VALIDADE: "Na validade",
        VENCIDO: "Vencido",
    };

    const statusStyles: Record<string, string> = {
        NA_VALIDADE: "bg-emerald-100 text-emerald-700",
        VENCIDO: "bg-rose-100 text-rose-700",
    };

    const statusBarras: Record<string, string> = {
        NA_VALIDADE: "bg-gradient-to-r from-emerald-400 to-emerald-500",
        VENCIDO: "bg-gradient-to-r from-rose-400 to-rose-500",
    };

    const produtosFiltrados = produtos.filter((produto) =>
        produto.nome.toLowerCase().includes(busca.trim().toLowerCase())
        && (filtroStatus === "TODOS" || produto.status === filtroStatus)
    );

    const resumo = [
        { titulo: "Produtos", valor: produtos.length, estilo: "text-primary-700" },
        { titulo: "Sem estoque", valor: produtos.filter((produto) => produto.quantidadeEstoque === 0).length, estilo: "text-accent-600" },
        { titulo: "Vencidos", valor: produtos.filter((produto) => produto.status === "VENCIDO").length, estilo: "text-rose-600" },
    ];

    const filtros = [
        { valor: "TODOS", nome: "Todos" },
        { valor: "NA_VALIDADE", nome: "Na validade" },
        { valor: "VENCIDO", nome: "Vencidos" },
    ];

    return (
        <div className="w-full flex-1 bg-slate-50">
            <div className="flex flex-col gap-4 px-8 pt-10 pb-6 sm:flex-row sm:items-center sm:justify-between">
                <div>
                    <h1 className="bg-gradient-to-r from-primary-900 to-primary-600 bg-clip-text text-3xl font-extrabold tracking-tight text-transparent">
                        Gestão de Produtos
                    </h1>
                    <p className="mt-1 text-sm text-slate-500">Gerencie o catálogo de produtos do sistema</p>
                </div>
                <Link
                    href="/produtos/novo"
                    className="inline-flex w-fit items-center gap-2 rounded-full bg-gradient-to-r from-accent-500 to-accent-600 px-5 py-2.5 text-sm font-semibold text-white shadow-lg shadow-accent-500/30 transition hover:shadow-xl hover:shadow-accent-500/40 hover:brightness-110 active:scale-[0.98]"
                >
                    + Novo Produto
                </Link>
            </div>

            <div className="grid grid-cols-1 gap-3 px-8 pb-5 sm:grid-cols-3">
                {resumo.map((item) => (
                    <div key={item.titulo} className="rounded-2xl border border-slate-200 bg-white px-5 py-3 shadow-sm">
                        <p className="text-xs font-semibold uppercase tracking-wide text-slate-500">{item.titulo}</p>
                        <p className={`text-2xl font-extrabold ${item.estilo}`}>{item.valor}</p>
                    </div>
                ))}
            </div>

            <div className="flex flex-col gap-4 px-8 pb-6 md:flex-row md:items-center md:justify-between">
                <input
                    value={busca}
                    onChange={(e) => dispatch(definirBusca(e.target.value))}
                    placeholder="Buscar produto pelo nome..."
                    className="w-full rounded-xl border border-slate-200 bg-white px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:outline-none focus:ring-4 focus:ring-primary-100 md:max-w-md"
                />

                <div className="flex gap-2">
                    {filtros.map((filtro) => (
                        <button
                            key={filtro.valor}
                            onClick={() => dispatch(definirStatus(filtro.valor))}
                            className={`rounded-full border px-4 py-2 text-sm font-semibold transition active:scale-[0.98] ${
                                filtroStatus === filtro.valor
                                ? "border-primary-600 bg-primary-600 text-white shadow-md shadow-primary-500/30"
                                : "border-slate-200 bg-white text-slate-600 hover:border-primary-200 hover:bg-primary-50 hover:text-primary-700"}`}
                        >
                            {filtro.nome}
                        </button>
                    ))}
                </div>
            </div>

            <div className="px-8 pb-10">
                {carregando && (
                    <div className="rounded-3xl border border-slate-200 bg-white px-6 py-12 text-center text-slate-500 shadow-sm">
                        Carregando produtos...
                    </div>
                )}

                {!carregando && produtosFiltrados.length === 0 && (
                    <div className="rounded-3xl border border-slate-200 bg-white px-6 py-12 text-center text-slate-500 shadow-sm">
                        Nenhum produto encontrado
                    </div>
                )}

                <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3 2xl:grid-cols-4">
                    {produtosFiltrados.map((produto) => (
                        <div key={produto.id} className="group relative flex flex-col overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm transition hover:-translate-y-1 hover:border-primary-200 hover:shadow-xl">
                            <div className={`h-1.5 w-full ${statusBarras[produto.status]}`}></div>

                            <div className="flex flex-1 flex-col gap-3 p-4">
                                <div className="flex items-start justify-between gap-3">
                                    <div className="flex min-w-0 items-center gap-3">
                                        <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-xl bg-gradient-to-br from-primary-600 to-primary-500 text-sm font-bold text-white shadow-md shadow-primary-500/30">
                                            {produto.nome.charAt(0).toUpperCase()}
                                        </div>
                                        <div className="min-w-0">
                                            <h2 className="truncate text-sm font-bold text-slate-800">{produto.nome}</h2>
                                            <p className="text-xs text-slate-400">Cód. {produto.id}</p>
                                        </div>
                                    </div>
                                    <span className={`inline-flex shrink-0 rounded-full px-2.5 py-0.5 text-xs font-semibold ${statusStyles[produto.status]}`}>
                                        {statusLabels[produto.status]}
                                    </span>
                                </div>

                                <p className="text-xl font-extrabold tracking-tight text-primary-700">{formatarMoeda(produto.preco)}</p>

                                <div className="grid grid-cols-2 gap-2">
                                    <div className="rounded-xl bg-slate-50 px-3 py-2">
                                        <p className="text-xs font-semibold uppercase tracking-wide text-slate-500">Estoque</p>
                                        <p className={`mt-0.5 text-xs font-bold ${produto.quantidadeEstoque === 0 ? "text-accent-600" : "text-slate-800"}`}>
                                            {produto.quantidadeEstoque === 0 ? "Sem estoque" : `${produto.quantidadeEstoque} un.`}
                                        </p>
                                    </div>
                                    <div className="rounded-xl bg-slate-50 px-3 py-2">
                                        <p className="text-xs font-semibold uppercase tracking-wide text-slate-500">Vencimento</p>
                                        <p className="mt-0.5 text-xs font-bold text-slate-800">{formatarData(produto.vencimento)}</p>
                                        <p className={`text-xs ${diasParaVencer(produto.vencimento) < 0 ? "text-rose-500" : "text-slate-400"}`}>{descreverVencimento(produto.vencimento)}</p>
                                    </div>
                                </div>
                            </div>

                            <div className="flex items-center justify-end gap-2 border-t border-slate-100 bg-slate-50/60 px-4 py-2">
                                <Link href={`/produtos/${produto.id}/editar`} className="rounded-full px-3 py-1 text-xs font-semibold text-primary-600 transition hover:bg-primary-50 hover:text-primary-700">Editar</Link>
                                <button onClick={() => handlerDeletarProduto(produto)} className="rounded-full px-3 py-1 text-xs font-semibold text-rose-500 transition hover:bg-rose-50 hover:text-rose-600">Excluir</button>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}
