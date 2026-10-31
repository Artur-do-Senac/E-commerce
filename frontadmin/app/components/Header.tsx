'use client'

import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../redux/store";
import { logout } from "../redux/slices/authSlice";

export default function Header(){

    const usuario = useSelector((state:RootState) => state.auth.usuario);

    const dispatch = useDispatch();

    // o usuário vem do cookie, que só existe no navegador: o nome só aparece depois de montar, para o HTML do servidor ser igual ao primeiro render do cliente
    const [montado, setMontado] = useState(false);

    useEffect(() => {
        setMontado(true);
    }, []);

    const handlerSair = () => {
        dispatch(logout());
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
                    <span className="text-sm font-semibold tracking-tight text-slate-700">{montado && usuario ? usuario.nome.toLocaleUpperCase() : 'Usuário não identificado!'}</span>
                </div>
                <button onClick={handlerSair} className="inline-flex items-center justify-center rounded-full border border-slate-200 bg-white px-5 py-2 text-sm font-semibold text-slate-600 shadow-sm transition hover:border-accent-500 hover:bg-accent-100 hover:text-accent-600 focus:outline-none focus:ring-4 focus:ring-accent-100 active:scale-[0.98]">Sair</button>
            </div>

        </header>


    </>);

}
