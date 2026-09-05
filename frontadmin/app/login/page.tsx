'use client'

import { useRouter } from "next/navigation"


export default function Login(){
    const router = useRouter();


    const handleLogin = async(formData:FormData) =>{
        

        router.push("/home")


    }



    return(<>
    <div className="relative isolate flex min-h-screen items-center justify-center overflow-hidden bg-primary-950 px-4 py-16 before:absolute before:-left-24 before:-top-24 before:h-72 before:w-72 before:rounded-full before:bg-accent-500/30 before:blur-3xl before:content-[''] after:absolute after:-bottom-24 after:-right-24 after:h-72 after:w-72 after:rounded-full after:bg-primary-500/40 after:blur-3xl after:content-['']">
        <div className="relative z-10 w-full max-w-md overflow-hidden rounded-3xl border border-white/10 bg-white/95 p-10 shadow-2xl shadow-black/40 backdrop-blur-xl before:absolute before:inset-x-0 before:top-0 before:h-1.5 before:bg-gradient-to-r before:from-primary-600 before:via-accent-500 before:to-primary-600 before:content-['']">
            <div className="mb-10 text-center">
                <h1 className="bg-gradient-to-r from-primary-900 to-primary-600 bg-clip-text text-3xl font-extrabold tracking-tight text-transparent sm:text-4xl">
                    Entrar no sistema
                </h1>
                <p className="mt-3 text-sm text-slate-500">
                    Insira suas credenciais para acessar o painel
                </p>
            </div>
            <form action={handleLogin} className="flex flex-col gap-6">
                <div className="group flex flex-col gap-2">
                    <label className="text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600">E-mail</label>
                    <input name = "email" placeholder="voce@empresa.com" className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100"/>
                </div>
                <div className="group flex flex-col gap-2">
                    <label className="text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600">Senha</label>
                    <input name = "senha" placeholder="*********" className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100"/>
                </div>
                <button type="submit" className="mt-4 inline-flex w-full items-center justify-center rounded-full bg-gradient-to-r from-accent-500 to-accent-600 px-6 py-3.5 text-sm font-semibold text-white shadow-lg shadow-accent-500/40 transition hover:shadow-xl hover:shadow-accent-500/50 hover:brightness-110 active:scale-[0.98]">Entrar</button>
            </form>

        </div>  
    </div>


    </>)
}