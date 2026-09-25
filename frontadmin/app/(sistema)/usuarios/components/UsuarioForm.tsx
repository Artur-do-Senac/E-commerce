'use client'

import { Usuario, UsuarioFormProp } from "@/app/types/usuario";
import axios from "axios";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function UsuarioForm({usuarioExistente}:UsuarioFormProp){

    const router = useRouter();

    const [usuario, setUsuario] = useState<Usuario>(usuarioExistente ?? new Usuario(null, "", "", "ATIVO", "", ""));
    
    const handlerChange = ( campo : 'nome' | 'email' | 'cpf' | 'senha', valor : string) =>{
        setUsuario(valorAnterior => new Usuario(valorAnterior.id, 
            campo === 'nome' ? valor : valorAnterior.nome, 
            campo === 'email' ? valor : valorAnterior.email,
            valorAnterior.status,
            campo === 'cpf' ? valor : valorAnterior.cpf,
            campo === 'senha' ? valor : valorAnterior.senha
            )        
        )
    }

    const handlerSalvar = async (formData : FormData) => {
        try {
            if (usuario.id) {
                await axios.put('http://localhost:8080/usuarios/'+usuario.id, usuario)
            } else {
                await axios.post('http://localhost:8080/usuarios', usuario)
            }
            alert("Usuário foi salvo com sucesso!");
        } catch (error) {
            alert("Erro ao salvar usuário");
            return;
        }

        router.push("/usuarios")
    }


    return (<>

    <form action={handlerSalvar} className="w-full">
        <div className="flex flex-col gap-6">
            <div className="group flex flex-col gap-2">
                <label className="text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600">Nome Completo: </label>
                <input 
                name="Nome"
                value={usuario.nome}
                required
                placeholder="Alcione Salomão..."
                onChange={(e) => handlerChange('nome', e.target.value)}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100"/>
            </div>
            <div className="group flex flex-col gap-2">
                <label className="text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600">CPF: </label>
                <input 
                name="CPF"
                required
                placeholder="000.000.000-00"
                value={usuario.cpf}
                onChange={(e) => handlerChange('cpf', e.target.value)}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100"/>
            </div>
            <div className="group flex flex-col gap-2">
                <label className="text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600">E-mail: </label>
                <input 
                name="email"
                value={usuario.email}
                required
                placeholder="email@email.com"
                onChange={(e) => handlerChange('email', e.target.value)}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100"/>
            </div>
            <div className="group flex flex-col gap-2">
                <label className="text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600">Senha: </label>
                <input 
                name="senha"
                value={usuario.senha}
                required
                placeholder="*******"
                type="password"
                onChange={(e) => handlerChange('senha', e.target.value)}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100"/>
            </div>


            <div className="mt-2 flex flex-col-reverse gap-3 sm:flex-row sm:items-center sm:justify-end">
                <Link href="/usuarios" className="inline-flex items-center justify-center rounded-full border border-slate-200 bg-white px-6 py-3 text-sm font-semibold text-slate-600 shadow-sm transition hover:border-accent-500 hover:bg-accent-100 hover:text-accent-600 focus:outline-none focus:ring-4 focus:ring-accent-100 active:scale-[0.98]">Cancelar</Link>
                <button type="submit" className="inline-flex items-center justify-center rounded-full bg-gradient-to-r from-accent-500 to-accent-600 px-8 py-3 text-sm font-semibold text-white shadow-lg shadow-accent-500/30 transition hover:shadow-xl hover:shadow-accent-500/40 hover:brightness-110 active:scale-[0.98]">Salvar</button>
            </div>


        </div>
    </form>


    </>);
}
