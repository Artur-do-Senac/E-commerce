'use client'
import Header from "../components/Header";
import Sidebar from "../components/Sidebar";
import Footer from "../components/Footer";
import { useSelector } from "react-redux";
import { RootState } from "../redux/store";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export default function SistemaLayout({children}){

    const usuario = useSelector((state:RootState) => state.auth.usuario);

    const router = useRouter();

    useEffect(()=>{
        if(usuario == null){
            router.push("/login");
        }
    }, [usuario, router]);

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
