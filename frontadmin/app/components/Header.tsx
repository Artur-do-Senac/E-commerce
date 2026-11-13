'use client'

import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../redux/store";
import { logout } from "../redux/slices/authSlice";
import Link from "next/link";
import Carrinho from "./Carrinho";

export default function Header(){

    const usuario = useSelector((state:RootState) => state.auth.usuario);

    const dispatch = useDispatch();

    // o usuário vem do cookie, que só existe no navegador: o nome só aparece depois de montar, para o HTML do servidor ser igual ao primeiro render do cliente
    const [montado, setMontado] = useState(false);

    // o carrinho é do cliente (USER): o botão mostra quantas unidades já estão nele
    const [carrinhoAberto, setCarrinhoAberto] = useState(false);
    const quantidadeNoCarrinho = useSelector((state:RootState) => state.carrinho.itens.reduce((soma, item) => soma + item.quantidade, 0));

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
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" className="h-5 w-5 fill-current">
                            <path fillRule="evenodd" clipRule="evenodd" d="M7.5 6a4.5 4.5 0 119 0 4.5 4.5 0 01-9 0zM3.751 20.105a8.25 8.25 0 0116.498 0 .75.75 0 01-.437.695A18.683 18.683 0 0112 22.5c-2.786 0-5.433-.608-7.812-1.7a.75.75 0 01-.437-.695z"/>
                        </svg>
                    </div>
                    {montado && usuario?.perfil === "USER" ? (
                        <Link href="/perfil" title="Meu cadastro" className="text-sm font-semibold tracking-tight text-slate-700 transition hover:text-primary-600">{usuario.nome.toLocaleUpperCase()}</Link>
                    ) : (
                        <span className="text-sm font-semibold tracking-tight text-slate-700">{montado && usuario ? usuario.nome.toLocaleUpperCase() : 'Usuário não identificado!'}</span>
                    )}
                </div>
                <div className="flex items-center gap-3">
                {montado && usuario?.perfil === "USER" && (
                    <button onClick={() => setCarrinhoAberto(true)} aria-label="Abrir carrinho" className="relative inline-flex h-10 w-10 items-center justify-center rounded-full border border-slate-200 bg-white text-slate-600 shadow-sm transition hover:border-primary-200 hover:bg-primary-50 hover:text-primary-700 focus:outline-none focus:ring-4 focus:ring-primary-100 active:scale-[0.98]">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" strokeLinecap="round" strokeLinejoin="round" className="h-5 w-5 fill-none stroke-current stroke-2">
                            <path d="M3 4h2l2.4 11.2a1 1 0 001 .8h8.9a1 1 0 001-.8L20 8H6"/>
                            <circle cx="9" cy="20" r="1.2"/>
                            <circle cx="17" cy="20" r="1.2"/>
                        </svg>
                        {quantidadeNoCarrinho > 0 && (
                            <span className="absolute -right-1 -top-1 flex h-5 min-w-5 items-center justify-center rounded-full bg-accent-500 px-1 text-[10px] font-bold text-white shadow-md shadow-accent-500/40">{quantidadeNoCarrinho}</span>
                        )}
                    </button>
                )}
                <button onClick={handlerSair} className="inline-flex items-center justify-center rounded-full border border-slate-200 bg-white px-5 py-2 text-sm font-semibold text-slate-600 shadow-sm transition hover:border-accent-500 hover:bg-accent-100 hover:text-accent-600 focus:outline-none focus:ring-4 focus:ring-accent-100 active:scale-[0.98]">Sair</button>
                </div>
            </div>

        </header>

        <Carrinho aberto={carrinhoAberto} onFechar={() => setCarrinhoAberto(false)} />


    </>);

}
