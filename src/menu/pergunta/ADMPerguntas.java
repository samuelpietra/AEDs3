// Classe de interface gráfica da administração das perguntas
/*
*	Criada por: Gustavo Lopes(MysteRys337)
*	Versão : 	0.0.1
*/
package menu.pergunta;

import produtos.*;
import menu.sistema.graficos.*;
import menu.sistema.Sistema;

public class ADMPerguntas {
	
		//Atributos
        private static ASCIInterface graficos; // Interface grafica feita em ASCII
        private static ANSILibrary   destaque;
    	private PerguntasCRUD perguntasCRUD;						 // Classe para interação com o banco de dados

		//Construtor
        public ADMPerguntas() 
        {
            perguntasCRUD = new PerguntasCRUD();
            graficos     = new ASCIInterface(199, 231 , 232, 184);
            destaque     = new ANSILibrary(15, 124, ANSILibrary.TEXTO_SUBLINHADO);
        }

         //Menu 2
    
        //Opção 1 : Tela de listagem de perguntas	
        /*
        *	Esta função serve para comunicar com o banco de dados
        *	e verificar se o usuário com o ID recebido possui perguntas
        *	registradas, caso essa pesquisa retorne verdadeiro, então
        *	ele irá chamar outra função para imprimir esse dados em uma
        *	string. Caso não tenha nada, a String de resposta irá retornar
        *	um aviso de erro.
        */
        public String listarPerguntas(int idUsuario) {

            String resp = "";
			Pergunta[] array  = perguntasCRUD.getPerguntaArray(idUsuario);

            if ( array == null ) {
                resp = "Ops.. parece que você não tem nenhuma pergunta...\n";
            
            } else {
                resp = listarPerguntas(array);
            
            }

            return resp;
        }
        
        //Função que retornar todas as perguntas inseridas em uma String
        private String listarPerguntas(Pergunta[] array) {

            String  resp     = "";
            byte    contador = 1;

            resp += graficos.caixa(3,"PERGUNTAS");

           
            for (Pergunta i : array) {
                if(i.getAtiva() == false) {
                    resp += "\n(Arquivada)";
                }

                resp += "\n" + destaque.imprimir(contador + ".") + "\n";
                resp += i.toString();
                contador++;
                
            }
    
            return resp;
        }
    
        //Opção 2: Criação de pergunta
        /*
        *   Primeiro o usuario precisa inserir a pergunta
        *   se a pergunta não for vazia, será perguntado para o mesmo
        *	se a pergunta que será registrada está correta. caso
        *	ele confirme então será passado para o banco de dados, o
        *	conteúdo que será registrado
        */
        public int novaPergunta(int idUsuario) {
    
            int idResp            = -1;
            String pergunta       = "";
            String palavrasChave  = "";
            String confirmar      = "";
    
            Pergunta p = null;
     
            System.out.println(graficos.caixa("Vamos criar uma nova pergunta"));
            System.out.print("\nPor favor insira a sua pergunta: ");
    
            pergunta = Sistema.lerEntrada();
    
            graficos.limparTela();
    
            if(!pergunta.equals("")) {

                System.out.println(graficos.caixa("Insira as palavras-chave associada a essa pergunta"));
                System.out.print("\n" + destaque.imprimir("OBS: lembre-se de separar as palavras-chave com um espaço em branco") + "\n->");
    
                palavrasChave = Sistema.lerEntrada();

                graficos.limparTela();

                if (!palavrasChave.equals("")) { 

                    p = new Pergunta(idUsuario,pergunta,palavrasChave);  
        
                    System.out.println(graficos.caixa("Vamos conferir a sua pergunta") + "\n");
                    System.out.print(p.toString() + 
                                    "\nEssa é a sua pergunta?(s/n) : ");
        
                    confirmar = Sistema.lerEntrada();
        
                    graficos.limparTela();
        
                    if(confirmar.length() == 0 || confirmar.toLowerCase().equals("s")) {
        
                        System.out.println("Certo! a sua pergunta foi criada!");                                 
                        idResp = perguntasCRUD.novaPergunta(p,idUsuario);
                        p.setId(idResp);
                    }
                    else {
        
                        System.out.println("Processo cancelado!\nVoltando para o menu...\n");
                    }
                }
    
            }
            else {
                System.err.println("ERRO! a pergunta inserida está vazia! Tente novamente!");
            }
    
            return idResp;
        }
    
        //Opção 3: Alterar a pergunta
        /*
        *	Essa função serve para encontrar as perguntas feitas
        *	pelo usuário e a partir de sua escolha na função
        *	"escolherPergunta(int IdUsuario)", ele pode
        *	alterar a pergunta e atualiza-la no banco de dados
        */
        public int alterarPergunta(int idUsuario) {
    
            int IdResposta          = -1;
            String entrada          = "";
            String palavrasChave    = "";
            String pergunta         = "";
    
            String palavrasChaveAux = "";

            Pergunta p              = null;
    
            System.out.println(graficos.caixa("Vamos alterar uma pergunta"));

            p = escolherPergunta(idUsuario);
        
            if( p != null && p.getAtiva() != false) {
        
                palavrasChaveAux = p.getPalavrasChave();

                System.out.print("Sucesso! Pergunta encontrada!\nVamos alterar a sua pergunta, pressione enter para continuar...");
                Sistema.lerEntrada();

                graficos.limparTela();
        
                System.out.println(graficos.caixa("Vamos criar uma nova pergunta"));
                System.out.print("\nPor favor insira a sua pergunta: ");
        
                pergunta = Sistema.lerEntrada();

                graficos.limparTela();
                        
                if(!pergunta.equals("")) {

                    System.out.println(graficos.caixa("Insira as palavras-chave associada a essa pergunta"));
                    System.out.print("\n" + destaque.imprimir("OBS: lembre-se de separar as palavras-chave com um espaço em branco") + "\n->");
        
                    palavrasChave = Sistema.lerEntrada();
    
                    graficos.limparTela();

                    if (!palavrasChave.equals("")) { 

                        p.setPergunta(pergunta);
                        p.setPalavrasChave(palavrasChave);
        
                        System.out.println(graficos.caixa("Vamos conferir a sua pergunta") + "\n");
                        System.out.print(p.toString() + 
                                        "\nEssa é a sua pergunta?(s/n) : ");
            
                        entrada = Sistema.lerEntrada();
            
                        graficos.limparTela();
            
                        if(entrada.length() == 0 || entrada.toLowerCase().equals("s")) {
            
                            System.out.println("Certo! a sua pergunta foi criada!");
                            perguntasCRUD.atualizarPergunta(p,palavrasChaveAux);
                            IdResposta = p.getId();
                        } 
                        else {
        
                            System.out.println("Processo cancelado!\nVoltando para o menu...\n");
                        }
                    }
                }
                else {
        
                System.out.println("Processo cancelado!\nVoltando para o menu...\n");
                }
            }
            else {
                System.err.println("ERRO! O valor inserido não é válido!\nVoltando ao menu...");
            }
            return IdResposta;
        }
    
        // Opção 4 : Arquivar pergunta  
        /*
        *	Essa função serve para encontrar as perguntas feitas
        *	pelo usuário e a partir de sua escolha na função
        *	"escolherPergunta(int IdUsuario)", ele pode
        *	querer arquivar a pergunta, que então é atualizada
        *	no banco de dados
        */
        public int arquivarPergunta(int IdUsuario) {
    
            int IdResposta       = -1;
            String entrada       = "";

            Pergunta p = null;

            System.out.println(graficos.caixa("Vamos alterar uma pergunta"));

            p = escolherPergunta(IdUsuario);
        
            if( p != null  &&  p.getAtiva() != false) {
        
                System.out.println("Sucesso! Pergunta encontrada!"           +
                                   "\nVamos imprimir essa pergunta:\n"         +
                                   p.toString()                              +
                                   "\n\nConfirme se essa é a pergunta?(s/n): ");
        
                entrada = Sistema.lerEntrada();

                graficos.limparTela();

                if(entrada.length() == 0 || entrada.toLowerCase().equals("s")) {
        
                    System.out.println("Certo! Vamos desativar essa pergunta");
                    p.setAtiva(false);
        
                    perguntasCRUD.desativarPergunta(p);

                    IdResposta = p.getId();
                            
                }     
            }
            else {
                System.out.println("Ok! Voltando menu...");
            }

            return IdResposta;
        }

        //Função para consultar as perguntas no sistema a partir das palavras-chave
        public void consultarPerguntas(int idUsuario) {

            String entrada    = "";
            Pergunta[] lista  = null;
            Pergunta tmp      = null;

            byte opcao        = -1;

            System.out.println(graficos.caixa("Vamos consultar por perguntas"));
            System.out.println(graficos.caixa(3,"Busque as perguntas por palavra chave separadas por espaço em branco"));
            System.out.print(destaque.imprimir("Ex: política Brasil eleições") + "\nPalavras chave: ");

            entrada = Pergunta.consertarPalavrasChave(Sistema.lerEntrada());

            lista = perguntasCRUD.getPerguntasPalavrasChave(entrada.split(" "),idUsuario);

            graficos.limparTela();

            if(lista != null) {

                System.out.println("Um total de " + lista.length + " foi/foram encontrado(s)");

                tmp = escolherPergunta(lista);

                graficos.limparTela();
                if ( tmp != null) {

                    System.out.println(graficos.caixa("Vamos imprimir a pergunta selecionada!"));

                    System.out.println(tmp.toString());
                    System.out.println("\n" + graficos.caixa("COMENTÁRIOS"));
                    System.out.println("\n" + graficos.caixa("RESPOSTAS"));
                    System.out.print(destaque.imprimir("Escolha uma das opções abaixo: ") +"\n\n" +
                                                       "1) Responder\n"                           +
                                                       "2) Comentar\n"                            +
                                                       "3) Avaliar\n\n"                           +
                                                       "0) Retornar\n\n"                          +
                                                       "Opção: ");
                    
                    opcao = (byte)Sistema.lerInt();

                }
            }
            else {
                System.out.println("Erro! Nenhuma pergunta encontrada com as palavras-chave inserida!");
            }
      
        }

		//Função intermediária: escolher uma pergunta
		/*
		*	Essa função serve como intermediário para que
		*	seja enviado uma ID de usuario, listar as perguntas
		*	do tal usuário e então escolher entre uma delas
		*/
        private Pergunta escolherPergunta(int IdUsuario) {

            Pergunta resp         = null;
            Pergunta[] array      = null;

            array = perguntasCRUD.getPerguntaArray(IdUsuario);         

            if ( array != null ) {

                resp = escolherPergunta(array);
            }
            else {
                System.err.println("ERRO! nenhuma pergunta encontrada!");
            }

            return resp; 
        }

        private Pergunta escolherPergunta(Pergunta[] array) {

            Pergunta resp             = null;
            byte     entrada          = -1;
            byte     indexSelecionado = -1;

            System.out.println(listarPerguntas(array));
        
            System.out.print("\nEscolha uma das suas perguntas: \nObs: Pressione \'0\' para voltar ao menu\n-> ");
            entrada = (byte)Sistema.lerInt();
        
            graficos.limparTela();           

            if (array.length > entrada - 1 && entrada - 1 >= 0) {

                    indexSelecionado = (byte)array[entrada -1].getId();
                    System.out.println("\nOk... vamos encontrar a sua pergunta.");
        
                    resp = perguntasCRUD.acharPergunta(indexSelecionado);
        
            }
            else {
                System.err.println("ERRO! Entrada inválida!");
            }

            return resp; 
        }

}
   
