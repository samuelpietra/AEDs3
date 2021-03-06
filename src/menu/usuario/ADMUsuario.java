package menu.usuario;

import produtos.*;
import seguranca.*;
import menu.sistema.Sistema;
import menu.sistema.graficos.*;

public class ADMUsuario {
    
    private static ASCIInterface graficos; // Interface grafica feita em ASCII
    private static ANSILibrary   destaque;
    private static ANSILibrary   destaque2;
    private UsuarioCRUD usuariosCRUD;

    public ADMUsuario () {
        usuariosCRUD = new UsuarioCRUD();
        graficos     = new ASCIInterface(202, 231 , 232, 184);
        destaque     = new ANSILibrary(15, 124, ANSILibrary.TEXTO_SUBLINHADO);
        destaque2    = new ANSILibrary(15, 27, ANSILibrary.TEXTO_SUBLINHADO);
    }

    //Menu 0

    //Opção 1: Acessando o sistema com um usuario já registrado
    /*
    *   Primeiro o usuario tenta inserir o e-mail, se o mesmo consta no banco
    *   de dados, então ele passa para a verificação de senha, se a senha
    *   não for válida, então o usuario não ganha acesso.
    */
    public int acessoAoSistema() {

        String email = "";
        String senha = "";
        int id       = -1;

        byte tentativas = 3;

        do {
            System.out.println(graficos.caixa(5,"Bem vindo usuário!"));
            System.out.print("ACESSO AO SISTEMA\n\nE-mail: ");

            email = Sistema.lerEntrada();
            
            graficos.limparTela();
            if(usuariosCRUD.acharUsuario(email) != null) {

                System.out.println(graficos.caixa(5,"Insira sua senha:"));
                System.out.print("\nACESSO AO SISTEMA\n\nSenha: ");
    
                senha = new GFG().senhaHasheada(Sistema.lerEntrada());
                Usuario user = usuariosCRUD.acharUsuario(email);

                graficos.limparTela();
                if (!user.getSenha().equals(senha)) { //conferir a senha no banco de dados

                    tentativas--;
                    System.out.println("Ops! Parece que essa não é a senha!\nTente novamente!\nN° de tentativas: " + tentativas + "\n");
                }
                else {

                    id = user.getId();
                }
            }
            else {

                if(email.equals("")) {
                    tentativas = 0;
                    System.out.println("\nProcesso cancelado!\nVoltando ao menu...");
                }
                else {
                    tentativas --;
                    System.out.println("\nO email inserido não é válido ou não existe no banco de dados\nTeste novamente!\nN° de tentativas: " + tentativas + "\n");
                }
            }
        } while( tentativas != 0  && id == -1);

        return id;
    }

    // Opção 2 : criar um novo usuario no CRUD
    /*
    *   Primeiro o usuario precisa digitar um email
    *   Caso o email ja esteja no banco de dados entao
    *   a leitura sera feita novamente. Apos isso, o
    *   email precisa ter confirmação por regex, ou
    *   seja, a formatação da String tem que estar
    *   de acordo com o formato de um email.
    *   Caso dê certo, então agora é hora de criar o
    *   usuario e pedir o nome e senha.
    *
    *   Por fim, o usuario irá confirmar se as credenciais estão certas
    *   se for confirmado, o CRUD irá registrar esse novo usuario, caso
    *   contrario, a operação será cancelada.
    */
    public boolean criandoUsuario() {

        String email     = "";
        String usuario   = "";
        String senha     = "";

        String confirmar = "";
        boolean resp     = false;

        do {
            System.out.println(graficos.caixa(5,"Vamos criar um novo usuário!"));
            System.out.print("NOVO USUÁRIO\n\nEmail: ");

            email = Sistema.lerEntrada();
                
            graficos.limparTela();
            
            if (usuariosCRUD.acharUsuario(email) != null) { //conferir se o email ja esta no banco de dados

                System.out.println("Esse e-mail já está registrado em nosso sistema\nTente outro e-mail!\n");
            }

        } while (usuariosCRUD.acharUsuario(email) != null); //enquanto o email nao estiver no banco de dados

        if(Sistema.verificarEmail(email)) {

            do {
                System.out.println(graficos.caixa(5,"Digite um nome e senha!"));
                System.out.print("NOVO USUÁRIO\n\nUsuário: ");

                usuario = Sistema.lerEntrada();

                if ( usuario.length() < 3) {

                    graficos.limparTela();
                    System.out.println(graficos.caixa(5,"Usuário inválido!"));
                    System.out.print("Tenha um usuário com no mínimo 3 caracteres!\n" +
                                         "Pressione \"enter\" para continuar...");
                    Sistema.lerEntrada();
                    graficos.limparTela();
                }

            } while( usuario.length() < 3 );

            do {
                
                System.out.print("\nSenha: ");
                senha = Sistema.lerEntrada();
                
                if (Sistema.verificarSenha(senha) <= 2)
                {
                    graficos.limparTela();
                    System.out.println(graficos.caixa("Não recomendamos que use esta senha!"));
                    System.out.println("Considere as recomendações abaixo para uma boa senha:\n" +
                                       "*   -> Ter mais de 8 dígitos\n" +
                                       "*   -> Ter algum caractere em minusculo\n" +
                                       "*   -> Ter algum caractere em maiusculo\n" +
                                       "*   -> Possuir algum caractere especial(Exemplo: *?#)\n" +
                                       "*   -> Possuir pelo menos 1 digito\n\n" +
                                       "Obs: Recomendamos no mínimo uma senha de força 3." + 
                                       "\nPressione enter para continuar...");
                    Sistema.lerEntrada();
                    graficos.limparTela();
                    System.out.println(graficos.caixa("Digite outra senha!"));

                }

            } while (Sistema.verificarSenha(senha) <= 2);

            graficos.limparTela();
            System.out.println(graficos.caixa("Vamos então verificar os seus dados!"));
            System.out.print("\n" +
                               destaque.imprimir("Email:           ") + destaque2.imprimir(email)     + "\n\n" +
                               destaque.imprimir("Nome de usuário: ") + destaque2.imprimir(usuario)   + "\n\n" +
                               destaque.imprimir("Senha:           ") + destaque2.imprimir(senha)     + "\n\n" +
                               "\nEstá tudo de acordo?(s/n) : "       );

            confirmar = Sistema.lerEntrada();

            graficos.limparTela();
            if(confirmar.length() == 0 || confirmar.toLowerCase().equals("s")) {

                System.out.println("Certo! Vamos então criar o usuario "+ usuario +" para você!");
                String senhah = new GFG().senhaHasheada(senha);
                Usuario user = new Usuario(usuario,email,senhah);
                int id = usuariosCRUD.novoUsuario(user);

                if(id != -1)
                {
                    resp = true;    
                }
                
            }
            else {

                System.out.println("Processo cancelado!\nVoltando para o menu...");
            }
        }
        else {
            System.err.println("O email inserido é inválido!\nVoltando ao menu...\n");
        }

        return resp;

    }
    
    //Opção 3: Mandar senha temporaria
    /*
    *   O programa irá pedir ao usuário que o mesmo
    *   insira o email usado pelo mesmo para registrar
    *   ao sistema. Se o email for encontrado, então
    *   um 'email' será enviado para o usuário, com
    *   a sua nova senha temporária.
    */
    public boolean senhaTemporaria() {

        Usuario user = new Usuario();

        String email          = "";
        String senhaTemp      = "";

        //boolean senhasIguais  = false;
        boolean resp = false;

        //byte forca = -1;

        System.out.println(graficos.caixa("Vamos resetar sua senha"));
        System.out.print("\nPor favor insira o seu email\n\nEmail: ");

        //Fazendo leitura do teclado
        email = Sistema.lerEntrada();

        graficos.limparTela();

        if(usuariosCRUD.acharUsuario(email) != null) {

            System.out.println("\nSucesso! uma mensagem com uma senha temporaria foi enviado\nao seu email!\n");
            senhaTemp = Sistema.gerarSenha();
            user = usuariosCRUD.acharUsuario(email);

            user.setSenha(new GFG().senhaHasheada(senhaTemp));
            usuariosCRUD.atualizarUsuario(user);            

            resp = true;

            Sistema.escreverEmail(senhaTemp,user.getNome());
            System.out.println("Obs: Procure por um .txt, na pasta do projeto ;)");
                
        }
        else {
            System.err.println("\nO email que foi inserido não é valido\nOu não consta em nosso banco de dados!\n Tente novamente!\n");
        }

        return resp;
    }

    //Menu 1

    //Opção 4 : Criação de uma novaSenha
    /*
    *   Caso o usuário esteja instatisfeito
    *   com a sua senha atual, ele pode pedir os sistema
    *   para atualizar a senha. Para isso o usuário terá que
    *   inserir a senha atual e então colocar a nova senha duas
    *   vezes para ter certeza que ele está ciente em qual é a senha
    */
    public boolean novaSenha(int IdUsuario) {

        Usuario user = null;

        String novaSenha      = "";
        String confirmarSenha = "";

        boolean senhasIguais  = false;
        boolean resp          = false;

        byte forca            = -1;

        System.out.println(graficos.caixa("Vamos resetar sua senha"));
        System.out.print("\nPor favor insira a sua senha atual.\nSenha: ");

        user = usuariosCRUD.acharUsuario(IdUsuario);

        confirmarSenha = Sistema.lerEntrada();

        graficos.limparTela();

        if(user.getSenha().equals(new GFG().senhaHasheada(confirmarSenha))) {

            do {
                System.out.println(graficos.caixa("Redefinindo senha!"));

                System.out.println("Nova senha: ");

                //Leitura da senha
                /*
                *   O usuario precisa fazer a inserção da senha
                *   duas vezes para conferir se o mesmo sabe
                *   qual e a senha
                */
                novaSenha = Sistema.lerEntrada();
                System.out.println("\nInsira novamente a senha: ");
                confirmarSenha = Sistema.lerEntrada();

                forca = Sistema.verificarSenha(novaSenha);
                senhasIguais = novaSenha.equals(confirmarSenha);

                graficos.limparTela();

                if ( senhasIguais == false ) {

                    System.err.println("ERRO!\nAs duas senhas inseridas não são iguais! Tente novamente\n");
                }
                if ( forca <= 2 || senhasIguais == false) {

                    System.err.println("ERRO!   Força da sua senha: " +  forca);
                    System.out.println("Considere as recomendações abaixo para uma boa senha:\n");
                    System.out.print("*   -> Ter mais de 8 dígitos\n" +
                                    "*   -> Ter algum caractere em minusculo\n" +
                                    "*   -> Ter algum caractere em maiusculo\n" +
                                    "*   -> Possuir algum caractere especial(Exemplo: *?#)\n" +
                                    "*   -> Possuir pelo menos 1 digito\n\n" +
                                    "Obs: Recomendamos no mínimo uma senha de força 3.\n" +                                      
                                    "Pressione \"Enter\" para continuar...");

                    Sistema.lerEntrada();
                    graficos.limparTela();

                }
            } while( senhasIguais == false || forca <= 2);

            user.setSenha(new GFG().senhaHasheada(novaSenha));
            usuariosCRUD.atualizarUsuario(user);            

            resp = true;

        }
        else {
            System.err.println("\nEssa não é a senha atual! Tente novamente!\n");
        }

        return resp;
    }

}