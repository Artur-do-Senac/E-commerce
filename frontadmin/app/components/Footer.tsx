export default function Footer(){

    const currentYear = new Date().getFullYear();

    return(<>

        <footer className="w-full border-t border-slate-200 bg-white px-8 py-4 text-center text-xs text-slate-500 sm:text-left">
            © {currentYear} MarketHub. Todos os direitos reservados.
        </footer>

    </>);
}
