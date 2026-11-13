'use client'

import { atualizarEnderecoUsuarioLogado } from "@/app/services/usuarioService";
import { buscarEnderecoPorCep } from "@/app/services/enderecoService";
import { login } from "@/app/redux/slices/authSlice";
import { RootState } from "@/app/redux/store";
import { EnderecoUsuario } from "@/app/types/usuario";
import Link from "next/link";
import { useRouter } from "next/navigation";
import { useRef, useState } from "react";
import { useDispatch, useSelector } from "react-redux";

const classeLabel = "text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600";

const classeInput = "w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100";

export default function MeuCadastro(){

    const router = useRouter();

    const dispatch = useDispatch();

    const usuario = useSelector((state:RootState) => state.auth.usuario);
    const token = useSelector((state:RootState) => state.auth.token);

    const [endereco, setEndereco] = useState<EnderecoUsuario>(usuario?.endereco ?? { cep: "", logradouro: "", numero: "", complemento: "", bairro: "", cidade: "", uf: "" });

    const [buscandoCep, setBuscandoCep] = useState(false);
    const [erroCep, setErroCep] = useState("");

    const campoNumero = useRef<HTMLInputElement>(null);

    const handlerChange = ( campo : keyof EnderecoUsuario, valor : string) =>{
        setEndereco(valorAnterior => ({ ...valorAnterior, [campo]: valor }));
    }

    // consulta o CEP no ViaCEP (pelo back) e preenche rua, bairro, cidade e UF
    const handlerBuscarCep = async (cep: string) => {

        setErroCep("");

        if (cep.replace(/\D/g, "").length !== 8) {
            setErroCep("Informe um CEP com 8 números.");
            return;
        }

        setBuscandoCep(true);

        try {
            const enderecoCep = await buscarEnderecoPorCep(cep);

            setEndereco(valorAnterior => ({
                ...valorAnterior,
                cep: enderecoCep.cep,
                logradouro: enderecoCep.logradouro ?? "",
                bairro: enderecoCep.bairro ?? "",
                cidade: enderecoCep.cidade ?? "",
                uf: enderecoCep.uf ?? ""
            }));

            campoNumero.current?.focus();
        } catch (error) {
            // o back responde 400 com a mensagem (ex.: "CEP invalido!") no corpo
            const mensagemBack = (error as { response?: { data?: unknown } }).response?.data;

            setErroCep(typeof mensagemBack === "string" && mensagemBack ? mensagemBack : "Não foi possível consultar o CEP.");
        } finally {
            setBuscandoCep(false);
        }
    }

    const handlerChangeCep = (valor: string) => {
        handlerChange("cep", valor);

        // ao completar os 8 números a consulta é feita sozinha
        if (valor.replace(/\D/g, "").length === 8) {
            handlerBuscarCep(valor);
        }
    }

    const handlerSalvar = async () => {

        try {
            const usuarioAtualizado = await atualizarEnderecoUsuarioLogado(endereco);

            // o usuário na store passa a ter o endereço (é ele que libera o "Confirmar pedido" no carrinho)
            dispatch(login({ token, usuario: usuarioAtualizado }));

            alert("Cadastro atualizado com sucesso!");
        } catch (error) {
            // o back responde 400 com a mensagem da validação (ex.: "Numero Invalido!") no corpo
            const mensagemBack = (error as { response?: { data?: unknown } }).response?.data;

            alert(typeof mensagemBack === "string" && mensagemBack ? mensagemBack : "Erro ao salvar cadastro");
            return;
        }

        router.push("/produtos")
    }

    return(<>

        <div className="w-full flex-1 bg-slate-50">
            <div className="mx-auto flex w-full max-w-2xl flex-col gap-8 px-8 pt-10 pb-10">
                <div>
                    <h1 className="bg-gradient-to-r from-primary-900 to-primary-600 bg-clip-text text-3xl font-extrabold tracking-tight text-transparent">Meu cadastro</h1>
                    <p className="mt-1 text-sm text-slate-500">Seus dados e o endereço de entrega dos pedidos.</p>
                </div>

                {!usuario?.endereco && (
                    <div className="rounded-2xl border border-accent-200 bg-accent-100 px-5 py-4 text-sm font-medium text-accent-600">
                        Complete seu cadastro com o endereço de entrega para poder finalizar pedidos.
                    </div>
                )}

                <div className="rounded-3xl border border-slate-200 bg-white p-8 shadow-sm">
                    <p className="mb-4 text-xs font-bold uppercase tracking-widest text-primary-600">Seus dados</p>
                    <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
                        <div>
                            <p className="text-xs font-semibold uppercase tracking-wide text-slate-400">Nome</p>
                            <p className="text-sm font-semibold text-slate-800">{usuario?.nome}</p>
                        </div>
                        <div>
                            <p className="text-xs font-semibold uppercase tracking-wide text-slate-400">E-mail</p>
                            <p className="text-sm font-semibold text-slate-800">{usuario?.email}</p>
                        </div>
                        <div>
                            <p className="text-xs font-semibold uppercase tracking-wide text-slate-400">CPF</p>
                            <p className="text-sm font-semibold text-slate-800">{usuario?.cpf}</p>
                        </div>
                    </div>
                </div>

                <div className="rounded-3xl border border-slate-200 bg-white p-8 shadow-sm">
                    <p className="mb-5 text-xs font-bold uppercase tracking-widest text-accent-600">Endereço de entrega</p>

                    <form action={handlerSalvar} className="flex flex-col gap-5">

                        <div className="group flex flex-col gap-2">
                            <label className={classeLabel}>CEP</label>
                            <div className="flex gap-3">
                                <input
                                name="cep"
                                value={endereco.cep}
                                required
                                placeholder="00000-000"
                                onChange={(e) => handlerChangeCep(e.target.value)}
                                className={classeInput}/>
                                <button
                                type="button"
                                onClick={() => handlerBuscarCep(endereco.cep)}
                                disabled={buscandoCep}
                                className="shrink-0 rounded-xl border border-primary-200 bg-primary-50 px-5 text-sm font-semibold text-primary-700 transition hover:bg-primary-100 active:scale-[0.98] disabled:cursor-not-allowed disabled:opacity-60"
                                >{buscandoCep ? "Buscando..." : "Buscar CEP"}</button>
                            </div>
                            {erroCep && <p className="text-xs font-medium text-rose-500">{erroCep}</p>}
                        </div>

                        <div className="group flex flex-col gap-2">
                            <label className={classeLabel}>Rua / Logradouro</label>
                            <input
                            name="logradouro"
                            value={endereco.logradouro}
                            required
                            onChange={(e) => handlerChange("logradouro", e.target.value)}
                            className={classeInput}/>
                        </div>

                        <div className="grid grid-cols-1 gap-5 sm:grid-cols-2">
                            <div className="group flex flex-col gap-2">
                                <label className={classeLabel}>Número</label>
                                <input
                                ref={campoNumero}
                                name="numero"
                                value={endereco.numero}
                                required
                                placeholder="123"
                                onChange={(e) => handlerChange("numero", e.target.value)}
                                className={classeInput}/>
                            </div>
                            <div className="group flex flex-col gap-2">
                                <label className={classeLabel}>Complemento</label>
                                <input
                                name="complemento"
                                value={endereco.complemento ?? ""}
                                placeholder="Apto, bloco..."
                                onChange={(e) => handlerChange("complemento", e.target.value)}
                                className={classeInput}/>
                            </div>
                        </div>

                        <div className="group flex flex-col gap-2">
                            <label className={classeLabel}>Bairro</label>
                            <input
                            name="bairro"
                            value={endereco.bairro}
                            required
                            onChange={(e) => handlerChange("bairro", e.target.value)}
                            className={classeInput}/>
                        </div>

                        <div className="grid grid-cols-3 gap-5">
                            <div className="group col-span-2 flex flex-col gap-2">
                                <label className={classeLabel}>Cidade</label>
                                <input
                                name="cidade"
                                value={endereco.cidade}
                                required
                                onChange={(e) => handlerChange("cidade", e.target.value)}
                                className={classeInput}/>
                            </div>
                            <div className="group flex flex-col gap-2">
                                <label className={classeLabel}>UF</label>
                                <input
                                name="uf"
                                value={endereco.uf}
                                required
                                maxLength={2}
                                placeholder="SC"
                                onChange={(e) => handlerChange("uf", e.target.value.toUpperCase())}
                                className={classeInput}/>
                            </div>
                        </div>

                        <div className="mt-2 flex flex-col-reverse gap-3 sm:flex-row sm:items-center sm:justify-end">
                            <Link href="/produtos" className="inline-flex items-center justify-center rounded-full border border-slate-200 bg-white px-6 py-3 text-sm font-semibold text-slate-600 shadow-sm transition hover:border-accent-500 hover:bg-accent-100 hover:text-accent-600 focus:outline-none focus:ring-4 focus:ring-accent-100 active:scale-[0.98]">Cancelar</Link>
                            <button type="submit" className="inline-flex items-center justify-center rounded-full bg-gradient-to-r from-accent-500 to-accent-600 px-8 py-3 text-sm font-semibold text-white shadow-lg shadow-accent-500/30 transition hover:shadow-xl hover:shadow-accent-500/40 hover:brightness-110 active:scale-[0.98]">Salvar</button>
                        </div>

                    </form>
                </div>
            </div>
        </div>

    </>);
}
