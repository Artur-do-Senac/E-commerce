'use client'

import Link from "next/link"
import { useRouter } from "next/navigation"
import { FormEvent, useEffect, useState } from "react"
import { cadastrarEmpresa, cadastrarUsuario } from "../services/authService"
import { buscarListaCategorias } from "../services/categoriaService"
import { Categoria } from "../types/categoria"
import SucessoAnimado from "../components/SucessoAnimado"

const classeLabel = "text-xs font-semibold uppercase tracking-wide text-slate-500 transition group-focus-within:text-primary-600";

const classeInput = "w-full rounded-xl border border-slate-200 bg-slate-50 px-4 py-3 text-sm text-slate-900 shadow-sm transition placeholder:text-slate-400 focus:border-primary-500 focus:bg-white focus:outline-none focus:ring-4 focus:ring-primary-100";

export default function Cadastro(){

    const router = useRouter();

    // começa como cadastro de cliente (USER); "É empresa?" troca para o cadastro de empresa (GERENTE + empresa)
    const [ehEmpresa, setEhEmpresa] = useState(false);

    const [categorias, setCategorias] = useState<Categoria[]>([]);
    const [erro, setErro] = useState("");
    const [enviando, setEnviando] = useState(false);
    const [sucesso, setSucesso] = useState(false);

    useEffect(() => {

        // os ramos só são necessários no cadastro de empresa
        if (!ehEmpresa || categorias.length > 0) {
            return;
        }

        const carregarCategorias = async () => {

            try {
                const dados = await buscarListaCategorias();

                setCategorias(dados);
            } catch (error) {
                setErro("Não foi possível carregar os ramos de atuação.");
            }
        }

        carregarCategorias();
    }, [ehEmpresa, categorias.length]);

    const handlerAlternarTipo = () => {
        setErro("");
        setEhEmpresa(valorAnterior => !valorAnterior);
    }

    const handlerCadastrar = async (evento: FormEvent<HTMLFormElement>) => {

        // onSubmit (e não form action) para o formulário não ser limpo quando der erro de validação
        evento.preventDefault();

        const formData = new FormData(evento.currentTarget);

        setErro("");

        const campo = (nome: string) => formData.get(nome)?.toString().trim() ?? "";

        if (campo("senha") !== campo("confirmarSenha")) {
            setErro("As senhas não conferem.");
            return;
        }

        const responsavel = {
            nome: campo("nome"),
            email: campo("email"),
            senha: campo("senha"),
            cpf: campo("cpf"),
            telefone: campo("telefone")
        };

        setEnviando(true);

        try {
            if (ehEmpresa) {
                await cadastrarEmpresa({
                    ...responsavel,
                    razaoSocial: campo("razaoSocial"),
                    cnpj: campo("cnpj"),
                    telefoneEmpresa: campo("telefoneEmpresa"),
                    categoriaId: campo("categoriaId") ? Number(campo("categoriaId")) : null
                });
            } else {
                await cadastrarUsuario(responsavel);
            }

            setSucesso(true);
        } catch (error) {
            // o back responde 400 com a mensagem da validação (ex.: "CPF Invalido!") no corpo
            const mensagemBack = (error as { response?: { data?: unknown } }).response?.data;

            setErro(typeof mensagemBack === "string" && mensagemBack ? mensagemBack : "Não foi possível concluir o cadastro. Tente novamente.");
        } finally {
            setEnviando(false);
        }
    }

    return(<>
    <div className="relative isolate flex min-h-screen items-center justify-center overflow-hidden bg-primary-950 px-4 py-12 before:absolute before:-left-24 before:-top-24 before:h-72 before:w-72 before:rounded-full before:bg-accent-500/30 before:blur-3xl before:content-[''] after:absolute after:-bottom-24 after:-right-24 after:h-72 after:w-72 after:rounded-full after:bg-primary-500/40 after:blur-3xl after:content-['']">
        <div className="relative z-10 w-full max-w-xl overflow-hidden rounded-3xl border border-white/10 bg-white/95 p-8 shadow-2xl shadow-black/40 backdrop-blur-xl before:absolute before:inset-x-0 before:top-0 before:h-1.5 before:bg-gradient-to-r before:from-primary-600 before:via-accent-500 before:to-primary-600 before:content-[''] sm:p-10">

            {sucesso ? (
                <div className="flex flex-col items-center gap-8 py-6">
                    <SucessoAnimado
                        titulo={ehEmpresa ? "Empresa cadastrada!" : "Conta criada!"}
                        descricao={ehEmpresa ? "Sua empresa e o seu acesso de gerente estão prontos. Entre para começar a cadastrar seus produtos." : "Tudo certo! Entre para conhecer os produtos e fazer o seu primeiro pedido."}
                    />
                    <button
                        onClick={() => router.push("/login")}
                        className="inline-flex w-full items-center justify-center rounded-full bg-gradient-to-r from-accent-500 to-accent-600 px-6 py-3.5 text-sm font-semibold text-white shadow-lg shadow-accent-500/40 transition hover:shadow-xl hover:shadow-accent-500/50 hover:brightness-110 active:scale-[0.98]"
                    >
                        Ir para o login
                    </button>
                </div>
            ) : (<>

            <div className="mb-8 text-center">
                <span className={`inline-flex rounded-full px-3 py-1 text-xs font-semibold transition ${ehEmpresa ? "bg-accent-100 text-accent-600" : "bg-primary-100 text-primary-700"}`}>
                    {ehEmpresa ? "Conta de empresa" : "Conta de cliente"}
                </span>
                <h1 className="mt-4 bg-gradient-to-r from-primary-900 to-primary-600 bg-clip-text text-3xl font-extrabold tracking-tight text-transparent sm:text-4xl">
                    Criar conta grátis
                </h1>
                <p className="mt-3 text-sm text-slate-500">
                    {ehEmpresa
                        ? "Cadastre sua empresa e o responsável que vai gerenciá-la"
                        : "Cadastre-se para conhecer os produtos e fazer seus pedidos"}
                </p>
            </div>

            <form onSubmit={handlerCadastrar} className="flex flex-col gap-5">

                <p className="text-xs font-bold uppercase tracking-widest text-primary-600">
                    {ehEmpresa ? "Dados do responsável (gerente)" : "Seus dados"}
                </p>

                <div className="group flex flex-col gap-2">
                    <label className={classeLabel}>Nome completo</label>
                    <input name="nome" required placeholder="Maria da Silva" className={classeInput}/>
                </div>

                <div className="grid grid-cols-1 gap-5 sm:grid-cols-2">
                    <div className="group flex flex-col gap-2">
                        <label className={classeLabel}>CPF</label>
                        <input name="cpf" required placeholder="000.000.000-00" className={classeInput}/>
                    </div>
                    <div className="group flex flex-col gap-2">
                        <label className={classeLabel}>Telefone</label>
                        <input name="telefone" placeholder="(47) 99999-9999" className={classeInput}/>
                    </div>
                </div>

                <div className="group flex flex-col gap-2">
                    <label className={classeLabel}>E-mail</label>
                    <input name="email" type="email" required placeholder="voce@email.com" className={classeInput}/>
                </div>

                <div className="grid grid-cols-1 gap-5 sm:grid-cols-2">
                    <div className="group flex flex-col gap-2">
                        <label className={classeLabel}>Senha</label>
                        <input name="senha" type="password" required placeholder="*********" className={classeInput}/>
                    </div>
                    <div className="group flex flex-col gap-2">
                        <label className={classeLabel}>Confirmar senha</label>
                        <input name="confirmarSenha" type="password" required placeholder="*********" className={classeInput}/>
                    </div>
                </div>

                <div className={`grid transition-all duration-500 ease-in-out ${ehEmpresa ? "grid-rows-[1fr] opacity-100" : "grid-rows-[0fr] opacity-0"}`}>
                    <div className="overflow-hidden" inert={!ehEmpresa}>
                        <div className="flex flex-col gap-5 border-t border-dashed border-slate-200 pt-5">

                            <p className="text-xs font-bold uppercase tracking-widest text-accent-600">Dados da empresa</p>

                            <div className="group flex flex-col gap-2">
                                <label className={classeLabel}>Razão social</label>
                                <input name="razaoSocial" required={ehEmpresa} placeholder="Minha Empresa Ltda" className={classeInput}/>
                            </div>

                            <div className="grid grid-cols-1 gap-5 sm:grid-cols-2">
                                <div className="group flex flex-col gap-2">
                                    <label className={classeLabel}>CNPJ</label>
                                    <input name="cnpj" required={ehEmpresa} placeholder="00.000.000/0000-00" className={classeInput}/>
                                </div>
                                <div className="group flex flex-col gap-2">
                                    <label className={classeLabel}>Telefone da empresa</label>
                                    <input name="telefoneEmpresa" placeholder="(47) 3333-3333" className={classeInput}/>
                                </div>
                            </div>

                            <div className="group flex flex-col gap-2">
                                <label className={classeLabel}>Ramo de atuação</label>
                                <select name="categoriaId" required={ehEmpresa} defaultValue="" className={classeInput}>
                                    <option value="" disabled>Selecione o ramo da empresa...</option>
                                    {categorias.map((categoria) => (
                                        <option key={categoria.id} value={categoria.id ?? ""}>{categoria.nome}</option>
                                    ))}
                                </select>
                            </div>

                        </div>
                    </div>
                </div>

                {erro && (
                    <div className="rounded-xl border border-rose-200 bg-rose-50 px-4 py-3 text-sm font-medium text-rose-600">
                        {erro}
                    </div>
                )}

                <button
                    type="submit"
                    disabled={enviando}
                    className="mt-1 inline-flex w-full items-center justify-center rounded-full bg-gradient-to-r from-accent-500 to-accent-600 px-6 py-3.5 text-sm font-semibold text-white shadow-lg shadow-accent-500/40 transition hover:shadow-xl hover:shadow-accent-500/50 hover:brightness-110 active:scale-[0.98] disabled:cursor-not-allowed disabled:opacity-60"
                >
                    {enviando ? "Cadastrando..." : ehEmpresa ? "Cadastrar empresa" : "Criar minha conta"}
                </button>
            </form>

            <div className="mt-6 flex flex-col items-center gap-2 text-sm text-slate-500">
                <p>
                    {ehEmpresa ? "Quer apenas comprar? " : "É empresa? "}
                    <button type="button" onClick={handlerAlternarTipo} className="font-semibold text-accent-600 transition hover:text-accent-500">
                        {ehEmpresa ? "Cadastre-se como cliente" : "Cadastre-se aqui"}
                    </button>
                </p>
                <p>
                    Já tem conta?{" "}
                    <Link href="/login" className="font-semibold text-primary-600 transition hover:text-primary-500">Entrar</Link>
                </p>
            </div>

            </>)}

        </div>
    </div>
    </>)
}
