"use client";

import { useState } from "react";

const navLinks = [
  { href: "#recursos", label: "Recursos" },
  { href: "#como-funciona", label: "Como funciona" },
  { href: "#historia", label: "Nossa história" },
];

const features = [
  {
    title: "Gestão de produtos",
    description:
      "Cadastre e organize seu catálogo com controle total sobre preços, validade e disponibilidade em estoque.",
    icon: IconBox,
  },
  {
    title: "Pedidos em tempo real",
    description:
      "Acompanhe cada pedido do recebimento até a conclusão, com status sempre atualizado para você e seu cliente.",
    icon: IconClipboard,
  },
  {
    title: "Estoque automático",
    description:
      "Ao concluir um pedido, a quantidade vendida é abatida do estoque automaticamente. Sem planilha, sem erro manual.",
    icon: IconSync,
  },
  {
    title: "Cada loja, seus dados",
    description:
      "Todo lojista visualiza e gerencia apenas os seus próprios produtos e pedidos, com total isolamento entre lojas.",
    icon: IconShield,
  },
];

const steps = [
  {
    number: "01",
    title: "Cadastre sua loja",
    description: "Crie sua conta de lojista e configure seu perfil em poucos minutos.",
  },
  {
    number: "02",
    title: "Adicione seus produtos",
    description: "Monte seu catálogo com preços, validade e quantidade em estoque.",
  },
  {
    number: "03",
    title: "Receba pedidos",
    description: "Empresas parceiras encontram e compram seus produtos direto na plataforma.",
  },
  {
    number: "04",
    title: "Estoque atualizado",
    description: "A cada pedido concluído, seu estoque se ajusta sozinho, em tempo real.",
  },
];

const stats = [
  { value: "500+", label: "Lojistas ativos" },
  { value: "12 mil", label: "Produtos cadastrados" },
  { value: "98%", label: "Pedidos sem erro de estoque" },
];

const orders: { nome: string; empresa: string; status: "Concluído" | "Em andamento" | "Pendente" }[] = [
  { nome: "Pedido #4821", empresa: "Grão Fino Alimentos", status: "Concluído" },
  { nome: "Pedido #4820", empresa: "Casa Bela Materiais", status: "Em andamento" },
  { nome: "Pedido #4819", empresa: "Vértice Distribuidora", status: "Pendente" },
];

const orderStatusStyles: Record<string, string> = {
  Concluído: "bg-emerald-100 text-emerald-700",
  "Em andamento": "bg-accent-100 text-accent-700",
  Pendente: "bg-slate-200 text-slate-600",
};

const footerColumns = [
  {
    title: "Produto",
    links: [
      { label: "Recursos", href: "#recursos" },
      { label: "Como funciona", href: "#como-funciona" },
      { label: "Nossa história", href: "#historia" },
    ],
  },
  {
    title: "Empresa",
    links: [
      { label: "Sobre nós", href: "#historia" },
      { label: "Contato", href: "#" },
      { label: "Carreiras", href: "#" },
    ],
  },
  {
    title: "Legal",
    links: [
      { label: "Termos de uso", href: "#" },
      { label: "Privacidade", href: "#" },
    ],
  },
];

export default function Home() {
  const [menuOpen, setMenuOpen] = useState(false);

  return (
    <div className="min-h-screen bg-white text-slate-900">
      <header className="sticky top-0 z-50 border-b border-slate-200/70 bg-white/80 backdrop-blur">
        <div className="mx-auto flex h-20 max-w-7xl items-center justify-between px-6">
          <a href="#top" className="flex items-center gap-2.5">
            <LogoMark className="h-9 w-9" />
            <span className="text-xl font-semibold tracking-tight text-primary-900">
              Market<span className="text-accent-500">Hub</span>
            </span>
          </a>

          <nav className="hidden items-center gap-8 md:flex">
            {navLinks.map((link) => (
              <a
                key={link.href}
                href={link.href}
                className="text-sm font-medium text-slate-600 transition hover:text-primary-700"
              >
                {link.label}
              </a>
            ))}
          </nav>

          <div className="hidden items-center gap-3 md:flex">
            <button
              type="button"
              className="inline-flex items-center justify-center rounded-full border border-primary-200 px-5 py-2.5 text-sm font-semibold text-primary-700 transition hover:bg-primary-50"
            >
              Entrar
            </button>
            <button
              type="button"
              className="inline-flex items-center justify-center rounded-full bg-accent-500 px-5 py-2.5 text-sm font-semibold text-white shadow-sm shadow-accent-500/30 transition hover:bg-accent-600"
            >
              Criar conta grátis
            </button>
          </div>

          <button
            type="button"
            aria-label="Abrir menu"
            aria-expanded={menuOpen}
            onClick={() => setMenuOpen((open) => !open)}
            className="inline-flex h-10 w-10 items-center justify-center rounded-lg border border-slate-200 text-slate-700 md:hidden"
          >
            {menuOpen ? <IconClose className="h-5 w-5" /> : <IconMenu className="h-5 w-5" />}
          </button>
        </div>

        {menuOpen && (
          <div className="border-t border-slate-200 bg-white px-6 py-4 md:hidden">
            <nav className="flex flex-col gap-4">
              {navLinks.map((link) => (
                <a
                  key={link.href}
                  href={link.href}
                  onClick={() => setMenuOpen(false)}
                  className="text-sm font-medium text-slate-600"
                >
                  {link.label}
                </a>
              ))}
            </nav>
            <div className="mt-5 flex flex-col gap-3">
              <button
                type="button"
                className="inline-flex items-center justify-center rounded-full border border-primary-200 px-5 py-2.5 text-sm font-semibold text-primary-700"
              >
                Entrar
              </button>
              <button
                type="button"
                className="inline-flex items-center justify-center rounded-full bg-accent-500 px-5 py-2.5 text-sm font-semibold text-white"
              >
                Criar conta grátis
              </button>
            </div>
          </div>
        )}
      </header>

      <main id="top">
        <section className="relative overflow-hidden bg-gradient-to-b from-primary-50 via-white to-white">
          <div className="mx-auto grid max-w-7xl items-center gap-16 px-6 py-20 lg:grid-cols-2 lg:py-28">
            <div>
              <span className="inline-flex items-center gap-2 rounded-full bg-primary-100 px-4 py-1.5 text-xs font-semibold uppercase tracking-wide text-primary-700">
                Plataforma de e-commerce B2B
              </span>
              <h1 className="mt-6 text-4xl font-bold tracking-tight text-primary-950 sm:text-5xl lg:text-6xl">
                O hub que conecta <span className="text-accent-500">lojistas</span> e empresas em um só lugar.
              </h1>
              <p className="mt-6 max-w-xl text-lg leading-relaxed text-slate-600">
                Gerencie produtos, receba pedidos e mantenha seu estoque sempre atualizado — automaticamente.
                Tudo o que o seu negócio B2B precisa, em uma única plataforma.
              </p>
              <div className="mt-8 flex flex-col gap-3 sm:flex-row">
                <button
                  type="button"
                  className="inline-flex items-center justify-center rounded-full bg-accent-500 px-7 py-3.5 text-sm font-semibold text-white shadow-lg shadow-accent-500/30 transition hover:bg-accent-600"
                >
                  Começar agora
                </button>
                <a
                  href="#como-funciona"
                  className="inline-flex items-center justify-center rounded-full border border-slate-300 px-7 py-3.5 text-sm font-semibold text-slate-700 transition hover:border-primary-400 hover:text-primary-700"
                >
                  Ver como funciona
                </a>
              </div>

              <dl className="mt-14 grid grid-cols-3 gap-6 border-t border-slate-200 pt-8">
                {stats.map((stat) => (
                  <div key={stat.label}>
                    <dt className="sr-only">{stat.label}</dt>
                    <dd className="text-2xl font-bold text-primary-900 sm:text-3xl">{stat.value}</dd>
                    <p className="mt-1 text-xs text-slate-500 sm:text-sm">{stat.label}</p>
                  </div>
                ))}
              </dl>
            </div>

            <div className="relative">
              <div className="absolute -left-6 -top-6 h-28 w-28 rounded-full bg-accent-200 opacity-70 blur-3xl" />
              <div className="absolute -bottom-8 -right-6 h-36 w-36 rounded-full bg-primary-200 opacity-70 blur-3xl" />

              <div className="relative rounded-2xl border border-slate-200 bg-white p-6 shadow-2xl shadow-primary-900/10">
                <div className="flex items-center justify-between border-b border-slate-100 pb-4">
                  <span className="text-sm font-semibold text-slate-900">Pedidos recentes</span>
                  <span className="rounded-full bg-primary-50 px-3 py-1 text-xs font-medium text-primary-700">
                    Nordeste Distribuidora
                  </span>
                </div>
                <ul className="mt-4 space-y-3">
                  {orders.map((order) => (
                    <li
                      key={order.nome}
                      className="flex items-center justify-between rounded-xl bg-slate-50 px-4 py-3"
                    >
                      <div>
                        <p className="text-sm font-semibold text-slate-900">{order.nome}</p>
                        <p className="text-xs text-slate-500">{order.empresa}</p>
                      </div>
                      <span
                        className={`rounded-full px-3 py-1 text-xs font-medium ${orderStatusStyles[order.status]}`}
                      >
                        {order.status}
                      </span>
                    </li>
                  ))}
                </ul>
                <div className="mt-5 grid grid-cols-3 gap-3 border-t border-slate-100 pt-4 text-center">
                  <div>
                    <p className="text-lg font-bold text-slate-900">128</p>
                    <p className="text-xs text-slate-500">Produtos</p>
                  </div>
                  <div>
                    <p className="text-lg font-bold text-slate-900">32</p>
                    <p className="text-xs text-slate-500">Estoque baixo</p>
                  </div>
                  <div>
                    <p className="text-lg font-bold text-slate-900">98%</p>
                    <p className="text-xs text-slate-500">No prazo</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>

        <section id="recursos" className="mx-auto max-w-7xl px-6 py-20 lg:py-28">
          <div className="mx-auto max-w-2xl text-center">
            <span className="inline-flex items-center gap-2 rounded-full bg-primary-100 px-4 py-1.5 text-xs font-semibold uppercase tracking-wide text-primary-700">
              Recursos
            </span>
            <h2 className="mt-4 text-3xl font-bold tracking-tight text-primary-950 sm:text-4xl">
              Feito para quem vive a rotina de vender entre empresas
            </h2>
            <p className="mt-4 text-lg text-slate-600">
              Menos planilha, menos e-mail perdido. Mais controle sobre cada produto, pedido e loja.
            </p>
          </div>

          <div className="mt-14 grid gap-6 sm:grid-cols-2 lg:grid-cols-4">
            {features.map((feature) => (
              <div
                key={feature.title}
                className="rounded-2xl border border-slate-200 bg-white p-6 transition hover:-translate-y-1 hover:shadow-lg hover:shadow-primary-900/5"
              >
                <div className="inline-flex h-11 w-11 items-center justify-center rounded-xl bg-primary-100 text-primary-700">
                  <feature.icon className="h-5 w-5" />
                </div>
                <h3 className="mt-5 text-base font-semibold text-slate-900">{feature.title}</h3>
                <p className="mt-2 text-sm leading-relaxed text-slate-600">{feature.description}</p>
              </div>
            ))}
          </div>
        </section>

        <section id="como-funciona" className="bg-primary-50/60 py-20 lg:py-28">
          <div className="mx-auto max-w-7xl px-6">
            <div className="mx-auto max-w-2xl text-center">
              <span className="inline-flex items-center gap-2 rounded-full bg-white px-4 py-1.5 text-xs font-semibold uppercase tracking-wide text-primary-700">
                Como funciona
              </span>
              <h2 className="mt-4 text-3xl font-bold tracking-tight text-primary-950 sm:text-4xl">
                Do cadastro ao pedido concluído, em quatro passos
              </h2>
            </div>

            <div className="mt-14 grid gap-8 md:grid-cols-4">
              {steps.map((step) => (
                <div key={step.number} className="relative rounded-2xl bg-white p-6 shadow-sm shadow-primary-900/5">
                  <span className="text-3xl font-bold text-primary-200">{step.number}</span>
                  <h3 className="mt-3 text-base font-semibold text-slate-900">{step.title}</h3>
                  <p className="mt-2 text-sm leading-relaxed text-slate-600">{step.description}</p>
                </div>
              ))}
            </div>
          </div>
        </section>

        <section id="historia" className="mx-auto max-w-7xl px-6 py-20 lg:py-28">
          <div className="grid gap-14 lg:grid-cols-2 lg:items-center">
            <div className="relative">
              <div className="absolute -left-8 -top-8 h-32 w-32 rounded-full bg-primary-100 blur-3xl" />
              <div className="relative rounded-3xl bg-gradient-to-br from-primary-700 to-primary-900 p-10 text-primary-50 shadow-xl">
                <IconQuote className="h-8 w-8 text-accent-400" />
                <p className="mt-6 text-lg font-medium leading-relaxed">
                  Por trás de cada pedido existe um lojista tentando crescer e uma empresa tentando comprar melhor.
                  Construímos a MarketHub para os dois lados dessa relação.
                </p>
                <p className="mt-6 text-sm font-semibold text-primary-200">— Equipe MarketHub</p>
              </div>
            </div>

            <div>
              <span className="inline-flex items-center gap-2 rounded-full bg-accent-100 px-4 py-1.5 text-xs font-semibold uppercase tracking-wide text-accent-600">
                Nossa história
              </span>
              <h2 className="mt-4 text-3xl font-bold tracking-tight text-primary-950 sm:text-4xl">
                Tudo começou com uma pergunta simples
              </h2>
              <div className="mt-6 space-y-4 text-base leading-relaxed text-slate-600">
                <p>
                  Por que negociar entre empresas ainda parece tão complicado? Enquanto o consumidor final ganhava
                  plataformas cada vez mais simples para comprar, o comércio entre empresas continuava preso a
                  e-mails soltos, planilhas desatualizadas e ligações intermináveis para confirmar se um produto
                  ainda estava em estoque.
                </p>
                <p>
                  A MarketHub nasceu para mudar isso. Começamos observando lojistas que perdiam tempo demais
                  organizando pedidos manualmente, e empresas compradoras que nunca sabiam ao certo se o que
                  precisavam ainda estava disponível.
                </p>
                <p>
                  Hoje, unimos os dois lados dessa relação em uma única plataforma: catálogo, pedidos e estoque
                  conectados, para que cada lojista possa focar no que faz de melhor — vender — e cada empresa
                  compre com confiança de que o que pediu vai chegar.
                </p>
              </div>
            </div>
          </div>
        </section>

        <section className="bg-primary-950">
          <div className="mx-auto max-w-5xl px-6 py-20 text-center lg:py-24">
            <h2 className="text-3xl font-bold tracking-tight text-white sm:text-4xl">
              Pronto para simplificar suas vendas B2B?
            </h2>
            <p className="mx-auto mt-4 max-w-xl text-base text-primary-200">
              Crie sua conta gratuitamente e comece a receber pedidos sem sair do controle do seu estoque.
            </p>
            <div className="mt-8 flex flex-col justify-center gap-3 sm:flex-row">
              <button
                type="button"
                className="inline-flex items-center justify-center rounded-full bg-accent-500 px-7 py-3.5 text-sm font-semibold text-white shadow-lg shadow-accent-500/30 transition hover:bg-accent-600"
              >
                Criar conta grátis
              </button>
              <button
                type="button"
                className="inline-flex items-center justify-center rounded-full border border-primary-700 px-7 py-3.5 text-sm font-semibold text-white transition hover:bg-primary-900"
              >
                Entrar
              </button>
            </div>
          </div>
        </section>
      </main>

      <footer className="bg-primary-900 text-primary-200">
        <div className="mx-auto max-w-7xl px-6 py-16">
          <div className="grid gap-10 lg:grid-cols-[1.4fr_1fr_1fr_1fr]">
            <div>
              <div className="flex items-center gap-2.5">
                <LogoMark className="h-9 w-9" />
                <span className="text-xl font-semibold tracking-tight text-white">
                  Market<span className="text-accent-400">Hub</span>
                </span>
              </div>
              <p className="mt-4 max-w-xs text-sm leading-relaxed text-primary-300">
                A plataforma B2B que conecta lojistas e empresas, do catálogo ao estoque, em um só lugar.
              </p>
            </div>

            {footerColumns.map((column) => (
              <div key={column.title}>
                <h4 className="text-sm font-semibold text-white">{column.title}</h4>
                <ul className="mt-4 space-y-3">
                  {column.links.map((link) => (
                    <li key={link.label}>
                      <a href={link.href} className="text-sm text-primary-300 transition hover:text-white">
                        {link.label}
                      </a>
                    </li>
                  ))}
                </ul>
              </div>
            ))}
          </div>

          <div className="mt-14 flex flex-col items-center justify-between gap-4 border-t border-primary-800 pt-8 sm:flex-row">
            <p className="text-xs text-primary-400">© 2026 MarketHub. Todos os direitos reservados.</p>
            <div className="flex items-center gap-4">
              <a href="#" aria-label="LinkedIn" className="text-primary-400 transition hover:text-white">
                <IconLinkedin className="h-5 w-5" />
              </a>
              <a href="#" aria-label="Instagram" className="text-primary-400 transition hover:text-white">
                <IconInstagram className="h-5 w-5" />
              </a>
            </div>
          </div>
        </div>
      </footer>
    </div>
  );
}

function LogoMark({ className }: { className?: string }) {
  return (
    <svg viewBox="0 0 40 40" className={className} aria-hidden="true">
      <defs>
        <linearGradient id="logoGradient" x1="0" y1="0" x2="40" y2="40" gradientUnits="userSpaceOnUse">
          <stop offset="0" stopColor="#2f5fde" />
          <stop offset="1" stopColor="#16234a" />
        </linearGradient>
      </defs>
      <rect width="40" height="40" rx="11" fill="url(#logoGradient)" />
      <line x1="12" y1="27" x2="20" y2="13" stroke="#ff9a5c" strokeWidth="2" strokeLinecap="round" />
      <line x1="20" y1="13" x2="28" y2="27" stroke="#ff9a5c" strokeWidth="2" strokeLinecap="round" />
      <line x1="12" y1="27" x2="28" y2="27" stroke="#ff9a5c" strokeWidth="2" strokeLinecap="round" />
      <circle cx="20" cy="13" r="3.5" fill="#ffffff" />
      <circle cx="12" cy="27" r="3.5" fill="#ff7a29" />
      <circle cx="28" cy="27" r="3.5" fill="#ff7a29" />
    </svg>
  );
}

function IconBox({ className }: { className?: string }) {
  return (
    <svg viewBox="0 0 24 24" fill="none" className={className} aria-hidden="true">
      <path
        d="M3.5 8.5L12 4l8.5 4.5v7L12 20l-8.5-4.5v-7Z"
        stroke="currentColor"
        strokeWidth="1.6"
        strokeLinejoin="round"
      />
      <path d="M3.8 8.4 12 12.7l8.2-4.3M12 12.7V20" stroke="currentColor" strokeWidth="1.6" strokeLinejoin="round" />
    </svg>
  );
}

function IconClipboard({ className }: { className?: string }) {
  return (
    <svg viewBox="0 0 24 24" fill="none" className={className} aria-hidden="true">
      <rect x="5" y="4.5" width="14" height="16" rx="2" stroke="currentColor" strokeWidth="1.6" />
      <path d="M9 4V3.5A1.5 1.5 0 0 1 10.5 2h3A1.5 1.5 0 0 1 15 3.5V4" stroke="currentColor" strokeWidth="1.6" />
      <path d="M8.5 12.5l2.2 2.2L15.5 10" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}

function IconSync({ className }: { className?: string }) {
  return (
    <svg viewBox="0 0 24 24" fill="none" className={className} aria-hidden="true">
      <path
        d="M4.5 12a7.5 7.5 0 0 1 12.6-5.5M19.5 12a7.5 7.5 0 0 1-12.6 5.5"
        stroke="currentColor"
        strokeWidth="1.6"
        strokeLinecap="round"
      />
      <path d="M17.5 3.5v3.5H14M6.5 20.5V17H10" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}

function IconShield({ className }: { className?: string }) {
  return (
    <svg viewBox="0 0 24 24" fill="none" className={className} aria-hidden="true">
      <path
        d="M12 3l7 3v5.2c0 4.4-3 7.9-7 9.3-4-1.4-7-4.9-7-9.3V6l7-3Z"
        stroke="currentColor"
        strokeWidth="1.6"
        strokeLinejoin="round"
      />
      <path d="M9 12l2.2 2.2L15.5 9.5" stroke="currentColor" strokeWidth="1.6" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
  );
}

function IconQuote({ className }: { className?: string }) {
  return (
    <svg viewBox="0 0 24 24" fill="currentColor" className={className} aria-hidden="true">
      <path d="M9.6 6.5c-3 1-5 3.6-5 6.9 0 2.6 1.7 4.4 3.9 4.4 1.9 0 3.3-1.4 3.3-3.2 0-1.7-1.2-3-2.8-3.1-.2 0-.4 0-.6.1.3-1.7 1.7-3.1 3.4-3.7l-2.2-1.4Zm9 0c-3 1-5 3.6-5 6.9 0 2.6 1.7 4.4 3.9 4.4 1.9 0 3.3-1.4 3.3-3.2 0-1.7-1.2-3-2.8-3.1-.2 0-.4 0-.6.1.3-1.7 1.7-3.1 3.4-3.7l-2.2-1.4Z" />
    </svg>
  );
}

function IconMenu({ className }: { className?: string }) {
  return (
    <svg viewBox="0 0 24 24" fill="none" className={className} aria-hidden="true">
      <path d="M4 6h16M4 12h16M4 18h16" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" />
    </svg>
  );
}

function IconClose({ className }: { className?: string }) {
  return (
    <svg viewBox="0 0 24 24" fill="none" className={className} aria-hidden="true">
      <path d="M6 6l12 12M18 6L6 18" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" />
    </svg>
  );
}

function IconLinkedin({ className }: { className?: string }) {
  return (
    <svg viewBox="0 0 24 24" fill="currentColor" className={className} aria-hidden="true">
      <path d="M6.94 8.5H4.06V19H6.94V8.5ZM5.5 4a1.6 1.6 0 1 0 0 3.2A1.6 1.6 0 0 0 5.5 4ZM19.94 19h-2.87v-5.2c0-1.24-.02-2.83-1.72-2.83-1.73 0-2 1.35-2 2.74V19H10.5V8.5h2.75v1.43h.04c.38-.72 1.32-1.48 2.72-1.48 2.9 0 3.44 1.9 3.44 4.38V19Z" />
    </svg>
  );
}

function IconInstagram({ className }: { className?: string }) {
  return (
    <svg viewBox="0 0 24 24" fill="none" className={className} aria-hidden="true">
      <rect x="3.5" y="3.5" width="17" height="17" rx="4.5" stroke="currentColor" strokeWidth="1.6" />
      <circle cx="12" cy="12" r="4" stroke="currentColor" strokeWidth="1.6" />
      <circle cx="17" cy="7" r="1" fill="currentColor" />
    </svg>
  );
}
