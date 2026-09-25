'use client'
import Link from "next/link";
import UsuarioForm from "../../components/UsuarioForm";
import { useParams, useRouter } from "next/navigation";
import { useEffect, useState } from "react";
import { Usuario } from "@/app/types/usuario";
import axios from "axios";

export default function EditarUsuario(){

    const router = useRouter();

    const parametro = useParams();

    const codigo = Number(parametro.codigo);

    const [usuario, setUsuario] = useState<Usuario | null>(null)

    useEffect(()=> {

        buscarDados();
        
    }, []);

    const buscarDados = async() => {

        try {
            const valorUsuarioBack = await axios.get<Usuario>('http://localhost:8080/usuarios/'+codigo);
            setUsuario(valorUsuarioBack.data)
        } catch (error) {
            alert("Usuário não encontrado!")
            router.push("/usuarios")
        }
    }

    if (!usuario) return(<div className="p-8"> Carregando dados...</div>)

    return(<>

        <div className="w-full flex-1 bg-slate-50">
            <div className="mx-auto flex w-full max-w-2xl flex-col gap-8 px-8 pt-10 pb-10">
                <div className="flex flex-col gap-4">
                    <Link href="/usuarios" className="group inline-flex w-fit items-center gap-2 rounded-full border border-slate-200 bg-white px-4 py-2 text-sm font-semibold text-slate-600 shadow-sm transition hover:border-primary-200 hover:bg-primary-50 hover:text-primary-700 focus:outline-none focus:ring-4 focus:ring-primary-100 active:scale-[0.98] before:content-['←'] before:transition-transform group-hover:before:-translate-x-0.5">Voltar</Link>

                    <div>
                        <h1 className="bg-gradient-to-r from-primary-900 to-primary-600 bg-clip-text text-3xl font-extrabold tracking-tight text-transparent">Editar Usuário {codigo}</h1>
                        <p className="mt-1 text-sm text-slate-500">Preencha os campos para editar usuário.</p>
                    </div>

                </div>

                <div className="rounded-3xl border border-slate-200 bg-white p-8 shadow-sm">
                    <UsuarioForm usuarioExistente = {usuario}/>
                </div>

            </div>

        </div>

    </>);
}
