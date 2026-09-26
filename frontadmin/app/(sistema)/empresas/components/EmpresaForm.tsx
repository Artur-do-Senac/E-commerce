'use client'

import { Empresa, EmpresaFormProp } from "@/app/types/empresa";
import axios from "axios";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function EmpresaForm({empresaExistente}:EmpresaFormProp){

    const router = useRouter();

    const [empresa, setEmpresa] = useState<Empresa>(empresaExistente ?? new Empresa(null, "", "", "ATIVO"));

    const handlerChange = ( campo : 'razaoSocial' | 'cnpj', valor : string) =>{
        setEmpresa(valorAnterior => new Empresa(valorAnterior.id,
            campo === 'razaoSocial' ? valor : valorAnterior.razaoSocial,
            campo === 'cnpj' ? valor : valorAnterior.cnpj,
            valorAnterior.status
            )
        )
    }

    const handlerSalvar = async () => {

        try {
            if (empresa.id) {
                await axios.put('http://localhost:8080/empresas/'+empresa.id, empresa)
            } else {
                await axios.post('http://localhost:8080/empresas', empresa)
            }
            alert("Empresa foi salva com sucesso!");
        } catch (error) {
            alert("Erro ao salvar empresa");
            return;
        }

        router.push("/empresas")
    }


    return (<>

    <form action={handlerSalvar} className="w-full">
        <div className="flex flex-col gap-6">
            <div className="group flex flex-col gap-2">
                <label className="text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600">Razão Social: </label>
                <input
                name="razaoSocial"
                value={empresa.razaoSocial}
                required
                placeholder="Empresa LTDA..."
                onChange={(e) => handlerChange('razaoSocial', e.target.value)}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100"/>
            </div>
            <div className="group flex flex-col gap-2">
                <label className="text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600">CNPJ: </label>
                <input
                name="cnpj"
                value={empresa.cnpj}
                required
                placeholder="00.000.000/0000-00"
                onChange={(e) => handlerChange('cnpj', e.target.value)}
                className="w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100"/>
            </div>


            <div className="mt-2 flex flex-col-reverse gap-3 sm:flex-row sm:items-center sm:justify-end">
                <Link href="/empresas" className="inline-flex items-center justify-center rounded-full border border-slate-200 bg-white px-6 py-3 text-sm font-semibold text-slate-600 shadow-sm transition hover:border-accent-500 hover:bg-accent-100 hover:text-accent-600 focus:outline-none focus:ring-4 focus:ring-accent-100 active:scale-[0.98]">Cancelar</Link>
                <button type="submit" className="inline-flex items-center justify-center rounded-full bg-gradient-to-r from-accent-500 to-accent-600 px-8 py-3 text-sm font-semibold text-white shadow-lg shadow-accent-500/30 transition hover:shadow-xl hover:shadow-accent-500/40 hover:brightness-110 active:scale-[0.98]">Salvar</button>
            </div>


        </div>
    </form>


    </>);
}
