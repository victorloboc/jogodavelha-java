package Aula13;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class JogodaVelha {
    //representa o arquivo a ser criado para armazenar os dados do ranking
    private static File arquivo = new File("ranking.obj"); 
    
    private static int jogador;
    private static int[][]tab = new int[3][3];
    private static int linha, coluna, vencedor;
    private static Scanner leitor = new Scanner(System.in);
    private static Jogador jogador1, jogador2;
    private static long inicio;
    private static Jogador[] listaJogadores = new Jogador[50];  //guardar todos os jogadores até o limite de 50
    private static int quantidadeJogadores = 0; //para contar quantos jogadores estão cadastrados
            
    
    //procedimento para desenhar o tabuleiro
    public static void desenharTabuleiro(int x, int y){
        if (tab[x][y] == 1){      
            //jogador 1 -> X
            System.out.print("X");
        }else if(tab[x][y] == 2){
            //jogador 2 -> O
            System.out.print("O");
        }else{
            System.out.print(" ");
        }
    }
    //procedimento para exibir o tabuleiro do jogo na tela
    public static void jogo(){
        System.out.print("\n  1    2   3\n");  //colunas do tabuleiro
        System.out.print("1 ");  //primeira linha
        desenharTabuleiro(0,0);  
        System.out.print("  | "); 
        desenharTabuleiro(0,1);
        System.out.print("  | ");
        desenharTabuleiro(0,2);
        System.out.print("\n ------------\n2 ");
        desenharTabuleiro(1,0);  
        System.out.print("  | "); 
        desenharTabuleiro(1,1);
        System.out.print("  | ");
        desenharTabuleiro(1,2);
        System.out.print("\n ------------\n3 ");
        desenharTabuleiro(2,0);  
        System.out.print("  | "); 
        desenharTabuleiro(2,1);
        System.out.print("  | ");
        desenharTabuleiro(2,2);                
    }
    //procedimento para fazer a jogada
    public static void jogar(int jog){
        int i=0;
        if(jog==1){
            jogador=1;
            System.out.println("\n\n Sua vez "+jogador1.nome);
        }else{
            System.out.println("\n\n Sua vez "+jogador2.nome);
            jogador=2;
        }
        //escolha do campo do tabuleiro em que fará a jogada
        while(i==0){
            linha = 0;
            coluna = 0;
            while(linha<1 || linha>3){
                System.out.println("Escolha a linha (1,2 ou 3): ");
                linha = leitor.nextInt();
                if(linha<1 || linha>3){
                    System.out.println("Linha inválida! Linha deve ser 1,2 ou 3.");
                }
            }
            while(coluna<1 || coluna>3){
                System.out.println("Escolha a coluna (1,2 ou 3): ");
                coluna = leitor.nextInt();
                if(coluna<1 || coluna>3){
                    System.out.println("Coluna inválida! Coluna deve 1, 2 ou 3.");
                }
            }
            //ajuste pois começa do zero
            linha=linha-1;
            coluna=coluna-1;
            //verificando se a posição está livre 
            if(tab[linha][coluna]==0){
                tab[linha][coluna] = jogador;
                i=1;
            }else{
                System.out.println("Posição ocupada.");
            }
        }
    }
    
    public static void checarResultado(){
        int i=0;
        //horizontal
        for(i=0;i<3;i++){
            if(tab[i][0]==tab[i][1] && tab[i][0]==tab[i][2]){
                if(tab[i][0]==1){
                    vencedor = 1;
                }else if(tab[i][0]==2){
                    vencedor = 2;
                }       
            }
        }
        //vertical
        for(i=0;i<3;i++){
            if(tab[0][i]==tab[1][i] && tab[0][i]==tab[2][i]){
                if(tab[0][i]==1){
                    vencedor=1;
                }else if(tab[0][i]==2){
                    vencedor=2;
                }
            }
        }
        //diagonal principal
        if(tab[0][0]==tab[1][1] && tab[0][0]==tab[2][2]){
            if(tab[0][i]==1){
                vencedor=1;
            }else if(tab[0][i]==2){
                vencedor=2;
            }
        }
        //diagonal secundária
        if(tab[0][2]==tab[1][1] && tab[0][2]==tab[2][0]){
            if(tab[0][2]==1){
                vencedor=1;
            }else if(tab[0][2]==2){
                vencedor=2;
            }
        }
    }
    public static void main(String[] args){
        lerJogadores();
        cadastro();
        tempoInicial();
        int i=0;
        for(i=1;i<10;i++){
            jogo();
            if(i%2==0){
                jogar(2);
            }else{
                jogar(1);
            }
            checarResultado();
            if(vencedor==1 || vencedor==2){
                i=10;
            }
        }
        jogo();
        System.out.println();
        if(vencedor==1){
            System.out.println("Parabéns "+jogador1.nome+", você ganhou!");
            jogador1.vitorias = jogador1.vitorias+1;
            jogador2.derrotas = jogador2.derrotas+1;
        }else if(vencedor==2){
            System.out.println("Parabéns "+jogador2.nome+", você ganhou!");
            jogador2.vitorias = jogador2.vitorias+1;
            jogador1.derrotas = jogador1.derrotas+1;
        }else{
            System.out.println("Empate!");
        }
        System.out.println("O tempo de jogo foi "+tempoFinal()+" s");
        imprimirJogadores();
        salvarJogadores();
    }
    
    //cadastro dos jogadores
    public static void cadastro(){
        System.out.println("Digite o nome do jogador 1");
        String nome_jogador1 = leitor.next();
        jogador1 = buscarJogador(nome_jogador1);
        System.out.println("Digite o nome do jogador 2");
        String nome_jogador2 = leitor.next();
        jogador2 = buscarJogador(nome_jogador2);
        //a função buscarJogador cria novos registros e recupera registros já existentes
    }
    
    //contagem do tempo da partida
    public static void tempoInicial(){
        inicio = System.currentTimeMillis(); //retornar um long que representa a data/hora atual em milissegundos.
    }
    public static long tempoFinal(){
        return(System.currentTimeMillis()-inicio)/1000;
    }
    
    //buscar e guardar os dados sobre os jogadores
    public static Jogador buscarJogador(String nome){
        Jogador jogadorBuscado = null; //null representa um valor vazio
        //verificar se ja existe o nome no registro
        for(int i=0;(i<quantidadeJogadores) && (jogadorBuscado==null);i++){
            if(listaJogadores[i]!=null && listaJogadores[i].nome.equalsIgnoreCase(nome)){   
                jogadorBuscado = listaJogadores[i];
            }
        }
        //caso não exista o nome no registro, criar um novo registro
        if(jogadorBuscado==null){
            jogadorBuscado = new Jogador();
            jogadorBuscado.nome = nome;
            if(quantidadeJogadores<50){
                listaJogadores[quantidadeJogadores] = jogadorBuscado;
                quantidadeJogadores = quantidadeJogadores+1;
            }
        }
        return jogadorBuscado;
    }
    
    //mostrar para o usuário a lista de jogadores e os resultados
    public static void imprimirJogadores(){
        System.out.println("****Resultados****");
        for(int i=0; i<quantidadeJogadores; i++){
            System.out.println("Nome: "+listaJogadores[i].nome);
            System.out.println("Vitorias: "+listaJogadores[i].vitorias);
            System.out.println("Derrotas: "+listaJogadores[i].derrotas);
        }
    }
    
    //manipulação de arquivos (dos registros salvos)
    public static void salvarJogadores(){
        try{
            ObjectOutputStream saida = new ObjectOutputStream(new FileOutputStream(arquivo));
            saida.writeObject(listaJogadores);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    public static void lerJogadores(){
        try{
            ObjectInputStream saida = new ObjectInputStream(new FileInputStream(arquivo));
            listaJogadores = (Jogador[]) saida.readObject();
            while (listaJogadores[quantidadeJogadores]!=null && quantidadeJogadores<50){
                quantidadeJogadores = quantidadeJogadores+1;
            }
        }catch (FileNotFoundException e){
            
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
