'use client'

import { useRouter } from "next/navigation";

export default function Header(){

    const router = useRouter();

    const handlerSair = () => {
        router.push("/login")
    }

    return(<>

        <header className="w-full border-b border-slate-200 bg-white/90 shadow-sm backdrop-blur">
            <div className="flex w-full items-center justify-between px-8 py-4">
                <div className="flex items-center gap-3">
                    <div className="flex h-10 w-10 items-center justify-center rounded-full bg-gradient-to-br from-primary-600 to-primary-500 text-white shadow-md shadow-primary-500/30 ring-2 ring-primary-100">
                        <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 fill-none stroke-current stroke-2">
                            <path d=""/>
                            <circle/>
                        </svg>
                    </div>
                    <span className="text-sm font-semibold tracking-tight text-slate-700"> Usuário Artur Seixas</span>
                </div>
                <button onClick={handlerSair} className="inline-flex items-center justify-center rounded-full border border-slate-200 bg-white px-5 py-2 text-sm font-semibold text-slate-600 shadow-sm transition hover:border-accent-500 hover:bg-accent-100 hover:text-accent-600 focus:outline-none focus:ring-4 focus:ring-accent-100 active:scale-[0.98]">Sair</button>
            </div>

        </header>


    </>);

}
