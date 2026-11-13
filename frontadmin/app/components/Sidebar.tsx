'use client'

import Link from "next/link";
import { useSelector } from "react-redux";
import { RootState } from "../redux/store";

export default function Sidebar(){

    const perfil = useSelector((state:RootState) => state.auth.usuario?.perfil);

    // cada perfil só vê no menu o que o back deixa ele acessar
    const links = [
        { nome: "Home", href: "/home", perfis: ["ADMIN", "GERENTE", "USER"] },
        { nome: "Usuários", href: "/usuarios", perfis: ["ADMIN"] },
        { nome: "Empresas", href: "/empresas", perfis: ["ADMIN", "GERENTE"] },
        { nome: "Categorias", href: "/categorias", perfis: ["ADMIN", "GERENTE"] },
        { nome: "Produtos", href: "/produtos", perfis: ["ADMIN", "GERENTE", "USER"] },
        { nome: "Pedidos", href: "/pedidos", perfis: ["ADMIN", "GERENTE"] },
        { nome: "Meu cadastro", href: "/perfil", perfis: ["USER"] },
    ];



    return(<>

        <aside className="sticky top-0 hidden h-screen w-64 shrink-0 flex-col border-r border-white/10 bg-primary-950 px-5 py-8 md:flex">
            <Link href="/home" className="mb-10 px-3 text-2xl font-extrabold tracking-tight text-white">
                Market<span className="text-accent-500">Hub</span>
            </Link>

            <nav className="flex flex-col gap-1">
                {links.filter((link) => perfil && link.perfis.includes(perfil)).map((link) => (
                    <Link
                        key={link.href}
                        href={link.href}
                        className="rounded-xl px-4 py-3 text-sm font-medium text-primary-200 transition hover:bg-primary-800 hover:text-white"
                    >
                        {link.nome}
                    </Link>
                ))}
            </nav>
        </aside>

    </>);
}
