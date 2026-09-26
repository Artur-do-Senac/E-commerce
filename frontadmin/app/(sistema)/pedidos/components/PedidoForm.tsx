'use client'

import { Pedido, PedidoFormProp } from "@/app/types/pedido";
import axios from "axios";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function PedidoForm({pedidoExistente}:PedidoFormProp){

    const router = useRouter();

    // o backend devolve a data como "2026-01-31T10:30:00", mas o input datetime-local só aceita "2026-01-31T10:30"
    const [pedido, setPedido] = useState<Pedido>(pedidoExistente
        ? new Pedido(pedidoExistente.id, pedidoExistente.valorTotal, pedidoExistente.data.slice(0, 16), pedidoExistente.desconto, pedidoExistente.status)
        : new Pedido(null, 0, "", 0, "PENDENTE"));

    const handlerChange = ( campo : 'valorTotal' | 'desconto' | 'data', valor : string) =>{
        setPedido(valorAnterior => new Pedido(valorAnterior.id,
            campo === 'valorTotal' ? Number(valor) : valorAnterior.valorTotal,
            campo === 'data' ? valor : valorAnterior.data,
            campo === 'desconto' ? Number(valor) : valorAnterior.desconto,
            valorAnterior.status
            )
        )
    }

    const handlerSalvar = async () => {

        try {
            if (pedido.id) {
                await axios.put('http://localhost:8080/pedidos/'+pedido.id, pedido)
            } else {
                await axios.post('http://localhost:8080/pedidos', pedido)
            }
            alert("Pedido foi salvo com sucesso!");
        } catch (error) {
            alert("Erro ao salvar pedido");
            return;
        }

        router.push("/pedidos")
    }


    return (<>

    <form action={handlerSalvar} className="w-full">
        <div className="flex flex-col gap-6">
            <div className="group flex flex-col gap-2">
                <label className="text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600">Valor Total: </label>
                <input
                name="valorTotal"
                type="number"
                step="0.01"
                min="0"
                value={pedido.valorTotal}
                required
                onChange={(e) => handlerChange('valorTotal', e.target.value)}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100"/>
            </div>
            <div className="group flex flex-col gap-2">
                <label className="text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600">Desconto: </label>
                <input
                name="desconto"
                type="number"
                step="0.01"
                min="0"
                value={pedido.desconto}
                required
                onChange={(e) => handlerChange('desconto', e.target.value)}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100"/>
            </div>
            <div className="group flex flex-col gap-2">
                <label className="text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600">Data: </label>
                <input
                name="data"
                type="datetime-local"
                value={pedido.data}
                required
                onChange={(e) => handlerChange('data', e.target.value)}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100"/>
            </div>


            <div className="mt-2 flex flex-col-reverse gap-3 sm:flex-row sm:items-center sm:justify-end">
                <Link href="/pedidos" className="inline-flex items-center justify-center rounded-full border border-slate-200 bg-white px-6 py-3 text-sm font-semibold text-slate-600 shadow-sm transition hover:border-accent-500 hover:bg-accent-100 hover:text-accent-600 focus:outline-none focus:ring-4 focus:ring-accent-100 active:scale-[0.98]">Cancelar</Link>
                <button type="submit" className="inline-flex items-center justify-center rounded-full bg-gradient-to-r from-accent-500 to-accent-600 px-8 py-3 text-sm font-semibold text-white shadow-lg shadow-accent-500/30 transition hover:shadow-xl hover:shadow-accent-500/40 hover:brightness-110 active:scale-[0.98]">Salvar</button>
            </div>


        </div>
    </form>


    </>);
}
