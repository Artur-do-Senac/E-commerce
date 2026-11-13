'use client'

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "../redux/store";
import { alterarQuantidade, limpar, remover } from "../redux/slices/carrinhoSlice";
import { salvarPedido } from "../services/pedidoService";
import SucessoAnimado from "./SucessoAnimado";

export default function Carrinho({aberto, onFechar}:{aberto:boolean, onFechar:()=>void}){

    const itens = useSelector((state:RootState) => state.carrinho.itens);

    const dispatch = useDispatch();

    const router = useRouter();

    // para comprar o cliente precisa ter completado o cadastro com o endereço de entrega
    const temEndereco = useSelector((state:RootState) => !!state.auth.usuario?.endereco);

    const [enviando, setEnviando] = useState(false);
    const [sucesso, setSucesso] = useState(false);
    const [erro, setErro] = useState("");

    // depois da animação de sucesso o carrinho (já vazio) fecha sozinho
    useEffect(() => {

        if (!sucesso) {
            return;
        }

        const temporizador = setTimeout(() => {
            setSucesso(false);
            onFechar();
        }, 3000);

        return () => clearTimeout(temporizador);
    }, [sucesso, onFechar]);

    const formatarMoeda = (valor: number) => valor.toLocaleString("pt-BR", { style: "currency", currency: "BRL" });

    const total = itens.reduce((soma, item) => soma + item.preco * item.quantidade, 0);

    // os itens aparecem agrupados pela empresa dona do produto
    const empresas = Array.from(new Set(itens.map((item) => item.empresaNome ?? "Sem empresa")));

    const handlerFechar = () => {
        setErro("");
        onFechar();
    }

    const handlerConfirmar = async () => {

        setErro("");
        setEnviando(true);

        try {
            await salvarPedido(itens.map((item) => ({ produtoId: item.produtoId, quantidade: item.quantidade })));

            dispatch(limpar());
            setSucesso(true);
        } catch (error) {
            // o back responde 400 com a mensagem da validação no corpo
            const mensagemBack = (error as { response?: { data?: unknown } }).response?.data;

            setErro(typeof mensagemBack === "string" && mensagemBack ? mensagemBack : "Não foi possível confirmar o pedido. Tente novamente.");
        } finally {
            setEnviando(false);
        }
    }

    return(<>

        <div
            onClick={handlerFechar}
            className={`fixed inset-0 z-40 bg-slate-900/40 backdrop-blur-sm transition-opacity duration-300 ${aberto ? "opacity-100" : "pointer-events-none opacity-0"}`}
        ></div>

        <aside className={`fixed right-0 top-0 z-50 flex h-full w-full max-w-md flex-col bg-white shadow-2xl transition-transform duration-300 ${aberto ? "translate-x-0" : "translate-x-full"}`}>

            <div className="flex items-center justify-between border-b border-slate-100 px-6 py-5">
                <div>
                    <h2 className="text-lg font-extrabold tracking-tight text-slate-900">Meu carrinho</h2>
                    <p className="text-xs text-slate-500">{itens.length} {itens.length === 1 ? "produto" : "produtos"}</p>
                </div>
                <button onClick={handlerFechar} aria-label="Fechar carrinho" className="inline-flex h-9 w-9 items-center justify-center rounded-full border border-slate-200 text-slate-500 transition hover:bg-slate-50 hover:text-slate-700">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" strokeLinecap="round" className="h-4 w-4 fill-none stroke-current stroke-2">
                        <path d="M6 6l12 12M18 6L6 18"/>
                    </svg>
                </button>
            </div>

            {sucesso ? (
                <div className="flex flex-1 items-center justify-center px-6">
                    <SucessoAnimado titulo="Pedido confirmado!" descricao="Seu pedido foi enviado com sucesso. Obrigado pela compra!" />
                </div>
            ) : (<>

                <div className="flex-1 overflow-y-auto px-6 py-4">
                    {itens.length === 0 && (
                        <div className="flex h-full flex-col items-center justify-center gap-2 text-center text-slate-400">
                            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" strokeLinecap="round" strokeLinejoin="round" className="h-12 w-12 fill-none stroke-current stroke-1">
                                <path d="M3 4h2l2.4 11.2a1 1 0 001 .8h8.9a1 1 0 001-.8L20 8H6"/>
                                <circle cx="9" cy="20" r="1.2"/>
                                <circle cx="17" cy="20" r="1.2"/>
                            </svg>
                            <p className="text-sm font-medium">Seu carrinho está vazio</p>
                            <p className="text-xs">Adicione produtos para fazer um pedido.</p>
                        </div>
                    )}

                    {empresas.map((empresa) => (
                        <div key={empresa} className="mb-5">
                            <p className="mb-2 text-xs font-bold uppercase tracking-widest text-primary-600">{empresa}</p>
                            <div className="flex flex-col gap-3">
                                {itens.filter((item) => (item.empresaNome ?? "Sem empresa") === empresa).map((item) => (
                                    <div key={item.produtoId} className="rounded-2xl border border-slate-200 bg-slate-50 p-3">
                                        <div className="flex items-start justify-between gap-3">
                                            <div className="min-w-0">
                                                <p className="truncate text-sm font-bold text-slate-800">{item.nome}</p>
                                                <p className="text-xs text-slate-500">{formatarMoeda(item.preco)} cada</p>
                                            </div>
                                            <button onClick={() => dispatch(remover(item.produtoId))} className="text-xs font-semibold text-rose-500 transition hover:text-rose-600">Remover</button>
                                        </div>
                                        <div className="mt-3 flex items-center justify-between">
                                            <div className="inline-flex items-center rounded-full border border-slate-200 bg-white">
                                                <button
                                                    onClick={() => dispatch(alterarQuantidade({ produtoId: item.produtoId, quantidade: item.quantidade - 1 }))}
                                                    disabled={item.quantidade <= 1}
                                                    className="h-8 w-8 text-base font-bold text-slate-600 transition hover:text-primary-600 disabled:cursor-not-allowed disabled:opacity-40"
                                                >-</button>
                                                <span className="w-8 text-center text-sm font-semibold text-slate-800">{item.quantidade}</span>
                                                <button
                                                    onClick={() => dispatch(alterarQuantidade({ produtoId: item.produtoId, quantidade: item.quantidade + 1 }))}
                                                    disabled={item.quantidade >= item.estoque}
                                                    className="h-8 w-8 text-base font-bold text-slate-600 transition hover:text-primary-600 disabled:cursor-not-allowed disabled:opacity-40"
                                                >+</button>
                                            </div>
                                            <p className="text-sm font-extrabold text-primary-700">{formatarMoeda(item.preco * item.quantidade)}</p>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>
                    ))}
                </div>

                <div className="border-t border-slate-100 px-6 py-5">
                    {erro && (
                        <div className="mb-4 rounded-xl border border-rose-200 bg-rose-50 px-4 py-3 text-sm font-medium text-rose-600">
                            {erro}
                        </div>
                    )}

                    <div className="mb-4 flex items-center justify-between">
                        <span className="text-sm font-semibold text-slate-500">Total</span>
                        <span className="text-2xl font-extrabold tracking-tight text-slate-900">{formatarMoeda(total)}</span>
                    </div>

                    {temEndereco ? (
                        <button
                            onClick={handlerConfirmar}
                            disabled={itens.length === 0 || enviando}
                            className="inline-flex w-full items-center justify-center rounded-full bg-gradient-to-r from-accent-500 to-accent-600 px-6 py-3.5 text-sm font-semibold text-white shadow-lg shadow-accent-500/40 transition hover:shadow-xl hover:shadow-accent-500/50 hover:brightness-110 active:scale-[0.98] disabled:cursor-not-allowed disabled:opacity-60"
                        >
                            {enviando ? "Confirmando..." : "Confirmar pedido"}
                        </button>
                    ) : (
                        <div className="flex flex-col gap-3">
                            <p className="rounded-xl border border-accent-200 bg-accent-100 px-4 py-3 text-xs font-medium text-accent-600">
                                Para finalizar o pedido, complete seu cadastro com o endereço de entrega.
                            </p>
                            <button
                                onClick={() => { handlerFechar(); router.push("/perfil"); }}
                                className="inline-flex w-full items-center justify-center rounded-full border border-primary-200 bg-primary-50 px-6 py-3.5 text-sm font-semibold text-primary-700 transition hover:bg-primary-100 active:scale-[0.98]"
                            >
                                Completar cadastro
                            </button>
                        </div>
                    )}
                </div>

            </>)}

        </aside>

    </>);
}
