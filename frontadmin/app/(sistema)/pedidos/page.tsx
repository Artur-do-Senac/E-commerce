'use client'

import { Pedido } from "@/app/types/pedido";
import { buscarListaPedidos, concluirItensPedido, deletarPedido } from "@/app/services/pedidoService";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { RootState } from "@/app/redux/store";

export default function Pedidos() {

    const [pedidos, setPedidos] = useState<Pedido[]>([])

    // enquanto a lista não chega do back, não faz sentido mostrar "Nenhum pedido encontrado"
    const [carregando, setCarregando] = useState(true)

    // o ADMIN enxerga os pedidos completos e pode excluir; o GERENTE enxerga só os itens dos produtos da empresa dele e conclui esses itens
    const ehAdmin = useSelector((state: RootState) => state.auth.usuario?.perfil === "ADMIN");
    const ehGerente = useSelector((state: RootState) => state.auth.usuario?.perfil === "GERENTE");

    useEffect(()=>{
        carregarDados();
    },[]);

    const carregarDados = async ()=>{

        try {
            const dados = await buscarListaPedidos();

            setPedidos(dados);
        } catch (error) {
            alert("Erro ao carregar dados")
        } finally {
            setCarregando(false)
        }

    }

    const handlerDeletarPedido = async(pedido: Pedido)=> {

        if (!confirm(`Deseja realmente excluir o pedido #${String(pedido.id).padStart(4, "0")}?`)) {
            return;
        }

        try {
            await deletarPedido(pedido.id || 0)
            alert("Pedido foi excluído com sucesso!");
        } catch (error) {
            alert("Erro ao excluir pedido");
            return;
        }

        carregarDados();
    }

    // o GERENTE conclui os itens dos produtos da empresa dele: o estoque desses produtos é baixado no back
    const handlerConcluirItens = async(pedido: Pedido)=> {

        if (!confirm(`Concluir os seus itens do pedido #${String(pedido.id).padStart(4, "0")}? O estoque dos produtos será baixado.`)) {
            return;
        }

        try {
            await concluirItensPedido(pedido.id || 0)
            alert("Itens concluídos com sucesso!");
        } catch (error) {
            // o back responde 400 com a mensagem (ex.: "Estoque insuficiente para Arroz!") no corpo
            const mensagemBack = (error as { response?: { data?: unknown } }).response?.data;

            alert(typeof mensagemBack === "string" && mensagemBack ? mensagemBack : "Erro ao concluir itens do pedido");
            return;
        }

        carregarDados();
    }

    const formatarMoeda = (valor: number) => valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });

    const formatarData = (data: string) => new Date(data).toLocaleString("pt-BR", { dateStyle: "short", timeStyle: "short" });

    const statusLabels: Record<string, string> = {
        CONCLUIDO: "Concluído",
        PENDENTE: "Pendente",
        CANCELADO: "Cancelado",
    };

    const statusStyles: Record<string, string> = {
        CONCLUIDO: "bg-emerald-100 text-emerald-700",
        PENDENTE: "bg-amber-100 text-amber-700",
        CANCELADO: "bg-rose-100 text-rose-700",
    };

    return (
        <div className="w-full flex-1 bg-slate-50">
            <div className="flex flex-col gap-4 px-8 pt-10 pb-6">
                <div>
                    <h1 className="bg-gradient-to-r from-primary-900 to-primary-600 bg-clip-text text-3xl font-extrabold tracking-tight text-transparent">
                        Gestão de Pedidos
                    </h1>
                    <p className="mt-1 text-sm text-slate-500">
                        {ehAdmin ? "Acompanhe os pedidos de todos os clientes" : "Pedidos dos clientes com produtos da sua empresa (os valores consideram só os seus produtos)"}
                    </p>
                </div>
            </div>

            <div className="px-8 pb-10">
                {carregando && (
                    <div className="rounded-3xl border border-slate-200 bg-white px-6 py-12 text-center text-slate-500 shadow-sm">
                        Carregando pedidos...
                    </div>
                )}

                {!carregando && pedidos.length === 0 && (
                    <div className="rounded-3xl border border-slate-200 bg-white px-6 py-12 text-center text-slate-500 shadow-sm">
                        Nenhum pedido encontrado
                    </div>
                )}

                <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
                    {pedidos.map((pedido) => (
                        <div key={pedido.id} className="flex flex-col overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm transition hover:border-primary-200 hover:shadow-lg">
                            <div className="flex items-start justify-between gap-3 border-b border-slate-100 px-5 py-4">
                                <div className="min-w-0">
                                    <p className="text-base font-extrabold text-slate-800">Pedido #{String(pedido.id).padStart(4, "0")}</p>
                                    <p className="truncate text-xs text-slate-500">{formatarData(pedido.data)}</p>
                                </div>
                                <span className={`inline-flex shrink-0 rounded-full px-2.5 py-0.5 text-xs font-semibold ${statusStyles[pedido.status]}`}>
                                    {statusLabels[pedido.status]}
                                </span>
                            </div>

                            {pedido.cliente && (
                                <div className="border-b border-slate-100 bg-slate-50/60 px-5 py-3">
                                    <p className="text-xs font-bold uppercase tracking-widest text-primary-600">Cliente</p>
                                    <p className="mt-1 text-sm font-semibold text-slate-800">{pedido.cliente.nome}</p>
                                    <p className="text-xs text-slate-500">{pedido.cliente.email}{pedido.cliente.telefone && ` · ${pedido.cliente.telefone}`}</p>
                                    {pedido.cliente.endereco ? (
                                        <p className="mt-1 text-xs text-slate-500">
                                            {pedido.cliente.endereco.logradouro}, {pedido.cliente.endereco.numero}{pedido.cliente.endereco.complemento && ` - ${pedido.cliente.endereco.complemento}`} · {pedido.cliente.endereco.bairro} · {pedido.cliente.endereco.cidade}/{pedido.cliente.endereco.uf} · CEP {pedido.cliente.endereco.cep}
                                        </p>
                                    ) : (
                                        <p className="mt-1 text-xs text-slate-400">Endereço não informado</p>
                                    )}
                                </div>
                            )}

                            <div className="flex-1 divide-y divide-slate-100 px-5">
                                {pedido.itens.map((item) => (
                                    <div key={item.produtoId} className="flex items-center justify-between gap-3 py-2.5">
                                        <div className="min-w-0">
                                            <p className="truncate text-sm font-semibold text-slate-800">{item.produtoNome}</p>
                                            <p className="truncate text-xs text-slate-400">
                                                {item.quantidade} x {formatarMoeda(item.precoUnitario)}{ehAdmin && ` · ${item.empresaNome ?? "Sem empresa"} · ${statusLabels[item.status]}`}
                                            </p>
                                        </div>
                                        <p className="shrink-0 text-sm font-bold text-slate-700">{formatarMoeda(item.subtotal)}</p>
                                    </div>
                                ))}
                            </div>

                            <div className="flex items-center justify-between gap-3 border-t border-slate-100 bg-slate-50/60 px-5 py-3">
                                <div>
                                    <p className="text-xs font-semibold uppercase tracking-wide text-slate-500">{ehAdmin ? "Valor total" : "Valor dos seus produtos"}</p>
                                    <p className="text-lg font-extrabold text-primary-700">{formatarMoeda(pedido.valorTotal)}</p>
                                </div>
                                {ehGerente && pedido.status === "PENDENTE" && (
                                    <button onClick={() => handlerConcluirItens(pedido)} className="rounded-full bg-gradient-to-r from-accent-500 to-accent-600 px-4 py-2 text-xs font-semibold text-white shadow-md shadow-accent-500/30 transition hover:shadow-lg hover:brightness-110 active:scale-[0.98]">Concluir meus itens</button>
                                )}
                                {ehAdmin && (
                                    <button onClick={() => handlerDeletarPedido(pedido)} className="rounded-full px-3 py-1 text-xs font-semibold text-rose-500 transition hover:bg-rose-50 hover:text-rose-600">Excluir</button>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}
