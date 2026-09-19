import Link from "next/link";

export default function Sidebar(){

    const links = [
        { nome: "Home", href: "/home" },
        { nome: "Usuários", href: "/usuarios" },
        { nome: "Empresas", href: "/empresas" },
        { nome: "Produtos", href: "/produtos" },
        { nome: "Pedidos", href: "/pedidos" },
    ];



    return(<>

        <aside className="sticky top-0 hidden h-screen w-64 shrink-0 flex-col border-r border-white/10 bg-primary-950 px-5 py-8 md:flex">
            <Link href="/home" className="mb-10 px-3 text-2xl font-extrabold tracking-tight text-white">
                Market<span className="text-accent-500">Hub</span>
            </Link>

            <nav className="flex flex-col gap-1">
                {links.map((link) => (
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
