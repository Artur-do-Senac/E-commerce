'use client'

import { Usuario } from "@/app/types/usuario";
import axios from "axios";
import Link from "next/link";
import { useEffect, useState } from "react";

export default function Usuarios() {
    
    const [usuarios, setUsuarios] = useState<Usuario[]>([])

    useEffect(()=>{
        carregarDados();
    },[]);

    const carregarDados = async ()=>{

        try {
        const dados = await axios.get<Usuario[]>("http://localhost:8080/usuarios")

        setUsuarios(dados.data);
    } catch (error) {
        alert("Erro ao carregar dados")
    }
        
    }

    const handlerAlterarStatusUsuario = async(usuario: Usuario)=> {

        const novoStatus = {
            statusUsuario: usuario.status === "ATIVO" ? "INATIVO" : "ATIVO"
        };

        try {
            await axios.patch('http://localhost:8080/usuarios/'+usuario.id+'/status', novoStatus)
            alert("Status atualizado com sucesso!");
        } catch (error) {
            alert("Erro ao atualizar status");
            return;
        }

        carregarDados();
    }

    const handlerDeletarUsuario = async(usuario: Usuario)=> {
        try {
            await axios.delete('http://localhost:8080/usuarios/'+usuario.id+'/excluir')
            alert("Usuário foi excluído com sucesso!");
        } catch (error) {
            alert("Erro ao excluir usuário");
            return;
        }

        carregarDados();
    }
    
    return (
        <div className="w-full flex-1 bg-slate-50">
            <div className="flex flex-col gap-4 px-8 pt-10 pb-6 sm:flex-row sm:items-center sm:justify-between">
                <div>
                    <h1 className="bg-gradient-to-r from-primary-900 to-primary-600 bg-clip-text text-3xl font-extrabold tracking-tight text-transparent">
                        Gestão de Usuários
                    </h1>
                    <p className="mt-1 text-sm text-slate-500">Gerencie os usuários cadastrados no sistema</p>
                </div>
                <Link
                    href="/usuarios/novo"
                    className="inline-flex w-fit items-center gap-2 rounded-full bg-gradient-to-r from-accent-500 to-accent-600 px-5 py-2.5 text-sm font-semibold text-white shadow-lg shadow-accent-500/30 transition hover:shadow-xl hover:shadow-accent-500/40 hover:brightness-110 active:scale-[0.98]"
                >
                    + Novo Usuário
                </Link>
            </div>

            <div className="px-8 pb-10">
                <div className="overflow-hidden rounded-3xl border border-slate-200 bg-white shadow-sm">
                    <div className="overflow-x-auto">
                        <table className="w-full text-left text-sm">
                            <thead className="bg-primary-50 text-xs font-semibold uppercase tracking-wide text-primary-700">
                                <tr>
                                <th className="px-6 py-4">Código</th>
                                    <th className="px-6 py-4">Nome</th>
                                    <th className="px-6 py-4">CPF</th>
                                    <th className="px-6 py-4">Email</th>
                                    <th className="px-6 py-4">Status</th>
                                    <th className="px-6 py-4 text-right">Ações</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-slate-100">
                                {usuarios.map((usuario) => (
                                    <tr key={usuario.id} className="transition hover:bg-slate-50">
                                        <td className="px-6 py-4 text-slate-600">{usuario.id}</td>
                                        <td className="px-6 py-4 font-medium text-slate-800">{usuario.nome}</td>
                                        <td className="px-6 py-4 text-slate-600">{usuario.cpf}</td>
                                        <td className="px-6 py-4 text-slate-600">{usuario.email}</td>
                                        <td className="px-6 py-4">
                                            <span className={`inline-flex rounded-full px-3 py-1 text-xs font-semibold ${
                                                usuario.status === 'ATIVO' ? 'bg-green-100 text-green-700'
                                                : 'bg-rose-100 text-rose-700'}`}>
                                                {usuario.status}
                                            </span>
                                        </td>
                                        <td className="px-6 py-4 text-right">
                                            <div className="flex justify-end gap-4">
                                                <Link href={`/usuarios/${usuario.id}/editar`} className="font-semibold text-primary-600 hover:text-primary-700">Editar</Link>
                                                <button onClick={() => handlerDeletarUsuario(usuario)} 
                                                className="font-medium transition-colors font-semibold text-rose-500 hover:text-rose-600"> Excluir</button>
                                                <button onClick = {()=> handlerAlterarStatusUsuario(usuario)}
                                       className= {`font-medium transition-colors ${usuario.status ==='INATIVO'
                                         ?'text-rose-500 hover:text-rose-600' 
                                         :'text-green-600 hover:text-green-800' }`
                                         }>
                                        {usuario.status}</button>
                                            </div>
                                        </td>
                                    </tr>
                                ))}

                                    {
                                        usuarios.length === 0 &&(
                                            <tr>
                                                <td colSpan={6}className="px-6 py-12 text-center">
                                                    Nenhum usuário encontrado
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
