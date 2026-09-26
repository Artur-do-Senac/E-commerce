'use client'

import { Produto, ProdutoFormProp } from "@/app/types/produto";
import axios from "axios";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function ProdutoForm({produtoExistente}:ProdutoFormProp){

    const router = useRouter();

    // o backend devolve o vencimento como "2026-01-31T00:00:00.000+00:00", mas o input date só aceita "2026-01-31"
    const [produto, setProduto] = useState<Produto>(produtoExistente
        ? new Produto(produtoExistente.id, produtoExistente.nome, produtoExistente.preco, produtoExistente.vencimento.slice(0, 10), produtoExistente.quantidadeEstoque, produtoExistente.status)
        : new Produto(null, "", 0, "", 0, "NA_VALIDADE"));

    const handlerChange = ( campo : 'nome' | 'preco' | 'quantidadeEstoque' | 'vencimento', valor : string) =>{
        setProduto(valorAnterior => new Produto(valorAnterior.id,
            campo === 'nome' ? valor : valorAnterior.nome,
            campo === 'preco' ? Number(valor) : valorAnterior.preco,
            campo === 'vencimento' ? valor : valorAnterior.vencimento,
            campo === 'quantidadeEstoque' ? Number(valor) : valorAnterior.quantidadeEstoque,
            valorAnterior.status
            )
        )
    }

    const handlerSalvar = async () => {

        try {
            if (produto.id) {
                await axios.put('http://localhost:8080/produtos/'+produto.id, produto)
            } else {
                await axios.post('http://localhost:8080/produtos', produto)
            }
            alert("Produto foi salvo com sucesso!");
        } catch (error) {
            alert("Erro ao salvar produto");
            return;
        }

        router.push("/produtos")
    }


    return (<>

    <form action={handlerSalvar} className="w-full">
        <div className="flex flex-col gap-6">
            <div className="group flex flex-col gap-2">
                <label className="text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600">Nome do Produto: </label>
                <input
                name="nome"
                value={produto.nome}
                required
                placeholder="Arroz 5kg..."
                onChange={(e) => handlerChange('nome', e.target.value)}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100"/>
            </div>
            <div className="group flex flex-col gap-2">
                <label className="text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600">Preço: </label>
                <input
                name="preco"
                type="number"
                step="0.01"
                min="0"
                value={produto.preco}
                required
                onChange={(e) => handlerChange('preco', e.target.value)}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100"/>
            </div>
            <div className="group flex flex-col gap-2">
                <label className="text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600">Estoque: </label>
                <input
                name="quantidadeEstoque"
                type="number"
                min="0"
                value={produto.quantidadeEstoque}
                required
                onChange={(e) => handlerChange('quantidadeEstoque', e.target.value)}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100"/>
            </div>
            <div className="group flex flex-col gap-2">
                <label className="text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600">Vencimento: </label>
                <input
                name="vencimento"
                type="date"
                value={produto.vencimento}
                required
                onChange={(e) => handlerChange('vencimento', e.target.value)}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100"/>
            </div>


            <div className="mt-2 flex flex-col-reverse gap-3 sm:flex-row sm:items-center sm:justify-end">
                <Link href="/produtos" className="inline-flex items-center justify-center rounded-full border border-slate-200 bg-white px-6 py-3 text-sm font-semibold text-slate-600 shadow-sm transition hover:border-accent-500 hover:bg-accent-100 hover:text-accent-600 focus:outline-none focus:ring-4 focus:ring-accent-100 active:scale-[0.98]">Cancelar</Link>
                <button type="submit" className="inline-flex items-center justify-center rounded-full bg-gradient-to-r from-accent-500 to-accent-600 px-8 py-3 text-sm font-semibold text-white shadow-lg shadow-accent-500/30 transition hover:shadow-xl hover:shadow-accent-500/40 hover:brightness-110 active:scale-[0.98]">Salvar</button>
            </div>


        </div>
    </form>


    </>);
}
