'use client'

import { Categoria } from "@/app/types/categoria";
import axios from "axios";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function Categorias() {

    const [categorias, setCategorias] = useState<Categoria[]>([])

    useEffect(()=>{

        const carregarDados = async ()=>{

            try {
                const dados = await axios.get<Categoria[]>("http://localhost:8080/categorias")

                setCategorias(dados.data);
            } catch (error) {
                alert("Erro ao carregar dados")
            }

        }

        carregarDados();
    },[]);

    const handlerDeletarCategoria = async(categoria: Categoria)=> {
        try {
            await axios.delete('http://localhost:8080/categorias/'+categoria.id+'/excluir')
            alert("Categoria foi excluída com sucesso!");
        } catch (error) {
            alert("Erro ao excluir categoria. Verifique se ela não está sendo usada por alguma empresa.");
            return;
        }

        setCategorias(listaAnterior => listaAnterior.filter(item => item.id !== categoria.id));
    }

    return (
        <div className="w-full flex-1 bg-slate-50">
            <div className="flex flex-col gap-4 px-8 pt-10 pb-6 sm:flex-row sm:items-center sm:justify-between">
                <div>
                    <h1 className="bg-gradient-to-r from-primary-900 to-primary-600 bg-clip-text text-3xl font-extrabold tracking-tight text-transparent">
                        Gestão de Categorias
                    </h1>
                    <p className="mt-1 text-sm text-slate-500">Gerencie as categorias (ramos) cadastradas no sistema</p>
                </div>
                <Link
                    href="/categorias/novo"
                    className="inline-flex w-fit items-center gap-2 rounded-full bg-gradient-to-r from-accent-500 to-accent-600 px-5 py-2.5 text-sm font-semibold text-white shadow-lg shadow-accent-500/30 transition hover:shadow-xl hover:shadow-accent-500/40 hover:brightness-110 active:scale-[0.98]"
                >
                    + Nova Categoria
                </Link>
            </div>

            <div className="px-8 pb-10">
                <div className="overflow-hidden rounded-3xl border border-slate-200 bg-white shadow-sm">
                    <div className="overflow-x-auto">
                        <table className="w-full text-left text-sm">
                            <thead className="bg-primary-50 text-xs font-semibold uppercase tracking-wide text-primary-700">
                                <tr>
                                    <th className="px-6 py-4">Nome</th>
                                    <th className="px-6 py-4">Descrição</th>
                                    <th className="px-6 py-4 text-right">Ações</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-slate-100">
                                {categorias.map((categoria) => (
                                    <tr key={categoria.id} className="transition hover:bg-slate-50">
                                        <td className="px-6 py-4 font-medium text-slate-800">{categoria.nome}</td>
                                        <td className="px-6 py-4 text-slate-600">{categoria.descricao}</td>
                                        <td className="px-6 py-4 text-right">
                                            <div className="flex justify-end gap-4">
                                                <Link href={`/categorias/${categoria.id}/editar`} className="font-semibold text-primary-600 hover:text-primary-700">Editar</Link>
                                                <button onClick={() => handlerDeletarCategoria(categoria)} className="font-semibold text-rose-500 hover:text-rose-600">Excluir</button>
                                            </div>
                                        </td>
                                    </tr>
                                ))}

                                {
                                    categorias.length === 0 &&(
                                        <tr>
                                            <td colSpan={3} className="px-6 py-12 text-center">
                                                Nenhuma categoria encontrada
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
