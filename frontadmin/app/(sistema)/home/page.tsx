import Link from "next/link";

export default function Home(){

    const modulos = [
        { nome: "Usuários", href: "/usuarios", descricao: "Gerencie os usuários do sistema" },
        { nome: "Empresas", href: "/empresas", descricao: "Gerencie as empresas cadastradas" },
        { nome: "Produtos", href: "/produtos", descricao: "Gerencie o catálogo de produtos" },
        { nome: "Pedidos", href: "/pedidos", descricao: "Acompanhe os pedidos realizados" },
    ];

    return (<>
        <div className="flex min-h-screen w-full flex-col items-center justify-center gap-10 bg-gradient-to-b from-primary-50 via-white to-white px-6 py-16 text-center">
            <h1 className="bg-gradient-to-r from-primary-900 to-primary-600 bg-clip-text text-4xl font-extrabold tracking-tight text-transparent sm:text-5xl"> Bem vindo ao MarketHub!</h1>

            <div className="grid w-full max-w-4xl grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
                {modulos.map((modulo) => (
                    <Link
                        key={modulo.href}
                        href={modulo.href}
                        className="group flex flex-col items-start gap-2 rounded-3xl border border-slate-200 bg-white/80 p-6 text-left shadow-sm backdrop-blur transition hover:-translate-y-1 hover:border-primary-200 hover:shadow-xl"
                    >
                        <span className="text-lg font-bold text-primary-700 transition group-hover:text-primary-600">{modulo.nome}</span>
                        <span className="text-sm text-slate-500">{modulo.descricao}</span>
                    </Link>
                ))}
            </div>
        </div>
    </>);
}