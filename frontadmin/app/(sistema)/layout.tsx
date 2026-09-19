import Header from "../components/Header";
import Sidebar from "../components/Sidebar";
import Footer from "../components/Footer";

export default function SistemaLayout({children}){
    return ( <>
    <div className="flex min-h-screen w-full bg-slate-50 text-slate-900">

        <Sidebar/>

        <div className="flex min-w-0 flex-1 flex-col">
            <Header/>

                <main className="flex w-full flex-1 flex-col">
                    {children}
                </main>

            <Footer/>

        </div>




    </div>
    </>);
}
