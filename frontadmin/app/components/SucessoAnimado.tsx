'use client'

import { useEffect, useState } from "react";

export default function SucessoAnimado({titulo, descricao}:{titulo:string, descricao?:string}){

    // o círculo e o check entram com transição (começam escondidos e aparecem logo após montar)
    const [visivel, setVisivel] = useState(false);

    useEffect(() => {
        const temporizador = setTimeout(() => setVisivel(true), 50);

        return () => clearTimeout(temporizador);
    }, []);

    return(<>

        <div className="flex flex-col items-center gap-4 text-center">
            <div className="relative flex h-24 w-24 items-center justify-center">
                <span className="absolute inline-flex h-full w-full animate-ping rounded-full bg-emerald-400/40 [animation-iteration-count:2]"></span>
                <span className={`relative flex h-24 w-24 items-center justify-center rounded-full bg-gradient-to-br from-emerald-400 to-emerald-600 text-white shadow-xl shadow-emerald-500/40 transition-all duration-700 ease-out ${visivel ? "scale-100 opacity-100" : "scale-0 opacity-0"}`}>
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" strokeLinecap="round" strokeLinejoin="round" className={`h-12 w-12 fill-none stroke-current stroke-3 transition-all delay-300 duration-700 ${visivel ? "scale-100 opacity-100" : "scale-50 opacity-0"}`}>
                        <path d="M5 13l4 4L19 7"/>
                    </svg>
                </span>
            </div>
            <h2 className="text-2xl font-extrabold tracking-tight text-slate-900">{titulo}</h2>
            {descricao && <p className="max-w-sm text-sm text-slate-500">{descricao}</p>}
        </div>

    </>);
}
