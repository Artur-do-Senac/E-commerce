import Link from "next/link";

export default function Usuarios(){
    
    return(<> 
        <div>
            <h1> Gestão de Usuários </h1>
            <Link href="/usuarios/novo"></Link>
        </div>
        <div>
            <div>
                <table>
                    <thead>
                        <tr>
                            <th> Nome</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Artur</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
     </>) 
}