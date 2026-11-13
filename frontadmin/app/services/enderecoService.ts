import { EnderecoCep } from "../types/usuario";
import api from "./api";

// consulta o CEP no ViaCEP (o back é quem chama o serviço externo)
export async function buscarEnderecoPorCep(cep:string) : Promise<EnderecoCep>{

    const dados = await api.get<EnderecoCep>('/endereco/'+cep.replace(/\D/g, ""));

    return dados.data;
}
