'use client'

import { Produto } from "@/app/types/produto";
import axios from "axios";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function Produtos() {

    const [produtos, setProdutos] = useState<Produto[]>([])

    useEffect(()=>{
        carregarDados();
    },[]);

    const carregarDados = async ()=>{

        try {
            const dados = await axios.get<Produto[]>("http://localhost:8080/produtos")

            setProdutos(dados.data);
        } catch (error) {
            alert("Erro ao carregar dados")
        }

    }

    const formatarMoeda = (valor: number) => valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });

    const formatarData = (data: string) => new Date(data).toLocaleDateString("pt-BR");

    const statusLabels: Record<string, string> = {
        NA_VALIDADE: "Na validade",
        VENCIDO: "Vencido",
    };

    const statusStyles: Record<string, string> = {
        NA_VALIDADE: "bg-emerald-100 text-emerald-700",
        VENCIDO: "bg-rose-100 text-rose-700",
    };

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

            <div className="px-8 pb-10">
                <div className="overflow-hidden rounded-3xl border border-slate-200 bg-white shadow-sm">
                    <div className="overflow-x-auto">
                        <table className="w-full text-left text-sm">
                            <thead className="bg-primary-50 text-xs font-semibold uppercase tracking-wide text-primary-700">
                                <tr>
                                    <th className="px-6 py-4">Produto</th>
                                    <th className="px-6 py-4">Preço</th>
                                    <th className="px-6 py-4">Estoque</th>
                                    <th className="px-6 py-4">Vencimento</th>
                                    <th className="px-6 py-4">Status</th>
                                    <th className="px-6 py-4 text-right">Ações</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-slate-100">
                                {produtos.map((produto) => (
                                    <tr key={produto.id} className="transition hover:bg-slate-50">
                                        <td className="px-6 py-4 font-medium text-slate-800">{produto.nome}</td>
                                        <td className="px-6 py-4 text-slate-600">{formatarMoeda(produto.preco)}</td>
                                        <td className="px-6 py-4 text-slate-600">{produto.quantidadeEstoque} un.</td>
                                        <td className="px-6 py-4 text-slate-600">{formatarData(produto.vencimento)}</td>
                                        <td className="px-6 py-4">
                                            <span className={`inline-flex rounded-full px-3 py-1 text-xs font-semibold ${statusStyles[produto.status]}`}>
                                                {statusLabels[produto.status]}
                                            </span>
                                        </td>
                                        <td className="px-6 py-4 text-right">
                                            <div className="flex justify-end gap-4">
                                                <Link href={`/produtos/${produto.id}/editar`} className="font-semibold text-primary-600 hover:text-primary-700">Editar</Link>
                                                <button className="font-semibold text-rose-500 hover:text-rose-600">Excluir</button>
                                            </div>
                                        </td>
                                    </tr>
                                ))}

                                {
                                    produtos.length === 0 &&(
                                        <tr>
                                            <td colSpan={6} className="px-6 py-12 text-center">
                                                Nenhum produto encontrado
                                            </td>
                                        </tr>
                                    )
                                }
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    );
}
