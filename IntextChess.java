
/*
R r rook
P p pawn
N n knight
B b bishop
K k king
Q q queen

  */
import java.util.Scanner;

class Main {
  static String[] moving = { "Capital", "Lowercase" };
  static String[] error = {"Not formated correctly","There is no piece at that position.","You cannot go to that position","You can't take your own piece!","You can't not move to the same position!","You can't move there, you will be in check!"};
  static char[][] board = new char[9][9];

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
     while(true){
      boolean gameOver = false;
      System.out.println(
          "Welcome to Chess. To move, type the position of your piece and the position it wants to go to seperated by a space. (ie \"e2 e4\"). Capital Letter goes first!");
      resetBoard();
      int turn = 0;
      while (true) {
        printboard();
        System.out.println("It is " + moving[turn % 2] + "'s turn.");
        String input = sc.nextLine();
        printSpace();
        if(checkFormat(input,turn%2)){
          char x1 = input.charAt(0);
          int y1 = input.charAt(1)-'0';
          char x2 = input.charAt(3);
          int y2 = input.charAt(4)-'0';
          char temp = get(x2,y2);
          set(x2,y2,get(x1,y1));
          set(x1,y1,'_');
          boolean checkCapital = check(0);
          boolean checkLowerCase = check(1);
          if(checkCapital){
            if(turn%2==1){
              System.out.println("Capital is in check!");
            }
            else{
              printError(5);
              set(x1,y1,get(x2,y2));
              set(x2,y2,temp);
              turn--;
            }
          }
          else if(checkLowerCase){
            if(turn%2==0){
              System.out.println("LowerCase is in check!");
            }
            else{
              printError(5);
              set(x1,y1,get(x2,y2));
              set(x2,y2,temp);
              turn--;
            }
          }
          if(checkmate(0)){
            System.out.println("LowerCase Won!");
            gameOver=true;
          }
          if(checkmate(1)){
            System.out.println("Capital Won!");
            gameOver=true;
          }
          turn++;
        }
        if(gameOver){
          break;
        }
      }  
       System.out.println("Play again? (y/n)");
       String a = sc.nextLine();
       if(!a.equals("y")){
         return;
       }
    }
  }

  static boolean checkmate(int which){
    if(which==0){
      if(!check(0)){
        return false;
      }
      int kingx = -1;
      int kingy = -1;
      for(int i = 1; i<=8; i++){
        for(int j=1; j<=8; j++){
          if(get(i,j)=='K'){
            kingx=i;
            kingy=j;
            break;
          }
        }
      }
      int[] x1 = {1,1,1,-1,-1,-1,0,0};
      int[] y1 = {1,0,-1,1,0,-1,1,-1};

      for(int i=0; i<8; i++){
        int x = kingx+x1[i];
        int y = kingy+y1[i];
        if(0<x&&x<=8&&0<y&&y<=8&&!isCapital(x,y)){
          char e = get(x,y);
          set(x,y,'K');
          set(kingx,kingy,'_');
          if(!check(0)){
            set(x,y,e);
            set(kingx,kingy,'K');
            return false;
          }
          set(x,y,e);
          set(kingx,kingy,'K');
        }
      }

      int attackerX = -1;
      int attackerY = -1;
      int attackerNum = 0;
      for(int i=0; i<=8; i++){
        for(int j=1; j<=8; j++){
          if(isLowerCase(i,j)&&canMove(i,j,kingx,kingy)){
            attackerNum++;
            attackerX = i;
            attackerY = j;
          }
        }
      }
      if(attackerNum>1){
        return true;
      }
      else{
        char attacker = get(attackerX,attackerY);

        if(canCapitalBlock(attacker,attackerX,attackerY)){
          return false;
          
        }

        if(attacker=='q'||attacker=='b'||attacker=='r'){
          int xAddition;
          int yAddition;
          if(attackerX-kingx==0){
            xAddition = 0;
          }
          else{
            xAddition = (kingx-attackerX)/(Math.abs(kingx-attackerX));
          }
          if(attackerY-kingy==0){
            yAddition = 0;
          }
          else{
            yAddition = (kingy-attackerY)/(Math.abs(kingy-attackerY));
          }
          int xpos = attackerX+xAddition;
          int ypos = attackerY+yAddition;
          while(get(xpos,ypos)!='K'){
            if(canCapitalBlock(attacker,xpos,ypos)){
              return false;
            }
            xpos+=xAddition;
            ypos+=yAddition;
          }
        }
      }
      return true;
    }
    else if(which==1){
      if(!check(1)){ 
        return false;
      }
      int kingx = -1;
      int kingy = -1;
      for(int i = 1; i<=8; i++){
        for(int j=1; j<=8; j++){
          if(get(i,j)=='k'){
            kingx=i;
            kingy=j;
            break;
          }
        }
      }
      int[] x1 = {1,1,1,-1,-1,-1,0,0};
      int[] y1 = {1,0,-1,1,0,-1,1,-1};

      for(int i=0; i<8; i++){
        int x = kingx+x1[i];
        int y = kingy+y1[i];
        if(0<x&&x<=8&&0<y&&y<=8&&!isLowerCase(x,y)){
          char e = get(x,y);
          set(x,y,'k');
          set(kingx,kingy,'_');
          if(!check(1)){
            set(x,y,e);
            set(kingx,kingy,'k');
            return false;
          }
          set(x,y,e);
          set(kingx,kingy,'k');
        }
      }

      int attackerX = -1;
      int attackerY = -1;
      int attackerNum = 0;
      for(int i=0; i<=8; i++){
        for(int j=1; j<=8; j++){
          if(isCapital(i,j)&&canMove(i,j,kingx,kingy)){
            attackerNum++;
            attackerX = i;
            attackerY = j;
          }
        }
      }
      if(attackerNum>1){
        return true;
      }
      else{
        char attacker = get(attackerX,attackerY);
        if(canLowercaseBlock(attacker,attackerX,attackerY)){
          return false;
        }

        if(attacker=='Q'||attacker=='B'||attacker=='R'){
          int xAddition;
          int yAddition;
          if(attackerX-kingx==0){
            xAddition = 0;
          }
          else{
            xAddition = (kingx-attackerX)/(Math.abs(kingx-attackerX));
          }
          if(attackerY-kingy==0){
            yAddition = 0;
          }
          else{
            yAddition = (kingy-attackerY)/(Math.abs(kingy-attackerY));
          }
          int xpos = attackerX+xAddition;
          int ypos = attackerY+yAddition;
          while(get(xpos,ypos)!='k'){
            if(canLowercaseBlock(attacker,xpos,ypos)){
              return false;
            }
            xpos+=xAddition;
            ypos+=yAddition;
          }
        }
      }
      return true;
    }
    return false;
  }

  static boolean canCapitalBlock(char attacker, int attackerX, int attackerY){

    for(int i=0; i<=8; i++){
      for(int j=1; j<=8; j++){
        if(isCapital(i,j)&&canMove(i,j,attackerX,attackerY)){
          set(attackerX,attackerY,get(i,j));
          set(i,j,'_');
          if(!check(0)){
            set(i,j,get(attackerX,attackerY));
            set(attackerX,attackerY,attacker);
            return true;
          }
          set(i,j,get(attackerX,attackerY));
          set(attackerX,attackerY,attacker);
        }
      }
    }
    return false;
  }
static boolean canLowercaseBlock(char attacker, int attackerX, int attackerY){
    for(int i=0; i<=8; i++){
      for(int j=1; j<=8; j++){
        if(isLowerCase(i,j)&&canMove(i,j,attackerX,attackerY)){
          set(attackerX,attackerY,get(i,j));
          set(i,j,'_');
          if(!check(1)){
            set(i,j,get(attackerX,attackerY));
            set(attackerX,attackerY,attacker);
            return true;
          }
          set(i,j,get(attackerX,attackerY));
          set(attackerX,attackerY,attacker);
        }
      }
    }
  return false;
  }







  
  static boolean check(int turn){
    if(turn == 0){
      int kingx = -1;
      int kingy = -1;
      for(int i = 1; i<=8; i++){
        for(int j=1; j<=8; j++){
          if(get(i,j)=='K'){
            kingx=i;
            kingy=j;
            break;
          }
        }
      }
      for(int i=0; i<=8; i++){
        for(int j=1; j<=8; j++){
          if(isLowerCase(i,j)&&canMove(i,j,kingx,kingy)){
            return true;
          }
        }
      }
    }
    else{
      int kingx=-1;
      int kingy=-1;
      for(int i = 1; i<=8; i++){
        for(int j=1; j<=8; j++){
          if(get(i,j)=='k'){
            kingx=i;
            kingy=j;
            break;
          }
        }
      }

      for(int i=0; i<=8; i++){
        for(int j=1; j<=8; j++){
          if(isCapital(i,j)&&canMove(i,j,kingx,kingy)){
            return true;
          }
        }
      }
    }
    return false;
  }

static void printSpace(){
  System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
}
  
  static void printboard() {
    System.out.println();
    System.out.println("  a b c d e f g h");
    System.out.println("  _ _ _ _ _ _ _ _");
    for (int i = 8; i > 0; i--) {
      System.out.print(i);
      for (int j = 1; j <= 8; j++) {
        System.out.print("|" + board[i][j]);
      }
      System.out.println("|" + i);
    }
    System.out.println("  a b c d e f g h");
  }



  
  static void resetBoard() {
    for (int i = 1; i <= 8; i++) {
      for (int j = 1; j <= 8; j++) {
        board[i][j] = '_';
      }
    }

    for (int i = 1; i <= 8; i++) { // pawns
      board[2][i] = 'P';
      board[7][i] = 'p';
    }
    set('a', 1, 'R');
    set('h', 1, 'R');
    set('a', 8, 'r');
    set('h', 8, 'r'); // rook
    set('b', 1, 'N');
    set('g', 1, 'N');
    set('b', 8, 'n');
    set('g', 8, 'n'); // knight
    set('c', 1, 'B');
    set('f', 1, 'B');
    set('c', 8, 'b');
    set('f', 8, 'b'); // bishop
    set('d', 1, 'Q');
    set('e', 1, 'K');
    set('d', 8, 'q');
    set('e', 8, 'k'); // king/queen
  }



  
  static void set(char x1, int y, char piece) {
    int x = x1 - 96;
    board[y][x] = piece;
  }
  static void set(int x, int y, char piece) {
    board[y][x] = piece;
  }
  static char get(char x1, int y) {
    int x = x1 - 96;
    return board[y][x];
  }
  static char get(int x, int y) {
    return board[y][x];
  }




  
  static boolean checkFormat(String input, int turn){
    if(input.length()!=5){
      printError(0);
      return false;
    }
    else if(input.charAt(2)!=' '){
      printError(0);
      return false;
    }
    if(input.substring(0,2).equals(input.substring(3,5))){
      printError(4);
      return false;
    }
    if(turn==0){ //Capital's turn
      return checkCapital(input);
    }
    else{ //Lowercase's turn
      return checkLowerCase(input);
    }
  }
  static boolean checkCapital(String input){
    int x = input.charAt(0)-'a'+1;
    int y = input.charAt(1)-'1'+1;
    //is the origional position ok?
    if(x<1||x>8||y<1||y>8||!isCapital(x,y)){
      printError(1);
      return false;
    }


    //Check if the piece can move to the second position
     int x1 = input.charAt(3)-'a'+1;
     int y1 = input.charAt(4)-'1'+1;
    if (x1<1||x1>8||y1<1||y1>8){
      printError(2);
      return false;
    }
    if(isCapital(x1,y1)){
      printError(3);
      return false;
    }
    if (!canMove(x,y,x1,y1)){
      printError(2);
      return false;
    }
    
    return true;
    
  }
   static boolean checkLowerCase(String input){
    int x = input.charAt(0)-'a'+1;
    int y = input.charAt(1)-'1'+1;
    //is the origional position ok?
    if(x<1||x>8||y<1||y>8||!isLowerCase(x,y)){
      printError(1);
      return false;
    }
    //Check if the piece can move to the second position
     int x1 = input.charAt(3)-'a'+1;
     int y1 = input.charAt(4)-'1'+1;
    if (x1<1||x1>8||y1<1||y1>8){
      printError(2);
      return false;
    }
    if(isLowerCase(x1,y1)){
      printError(3);
      return false;
    }
    if (!canMove(x,y,x1,y1)){
      printError(2);
      return false;
    }
     
     return true;
    
    
  }


  static boolean canMove(int x1,int y1, int x2, int y2){
    int piece = board[y1][x1];
    int diffx = Math.abs(x1-x2);
    int diffy = Math.abs(y1-y2);
    
    if(piece=='P'){
      if(diffx==0&&y2-y1==1&&isEmpty(x2,y2)){
        return true;
      }
      if(diffx==0&&y2-y1==2&&isEmpty(x1,y1+1)&&isEmpty(x1,y1+2)){
        return true;
      }
      if(diffx==1&&y2-y1==1&&isLowerCase(x2, y2)){
        return true;
      }
    }
    else if(piece=='p'){
      if(diffx==0&&y2-y1==-1&&isEmpty(x2,y2)){
        return true;
      }
      if(diffx==0&&y2-y1==-2&&isEmpty(x1,y1-1)&&isEmpty(x1,y1-2)){
        return true;
      }
      if(diffx==1&&y2-y1==-1&&isCapital(x2, y2)){
        return true;
      }
    }
    else if(piece=='R'||piece=='r'){
      return moveRook(x1,x2,y1,y2);
    }
    else if(piece=='N'||piece=='n'){
      if(diffx*diffy==2){
        return true;
      }
    }
    else if(piece=='B'||piece=='b'){
      return moveBishop(x1,x2,y1,y2);
    }
    else if(piece=='Q'||piece=='q'){
      return moveBishop(x1,x2,y1,y2)||moveRook(x1,x2,y1,y2);
    }
    else if(piece=='K'||piece=='k'){
      if(diffx<=1&&diffy<=1){
        return true;
      }
    }
      return false;
  }
  static boolean moveRook(int x1, int x2, int y1, int y2){
    if(x1-x2==0){
      int smaller = Math.min(y1,y2);
      int larger = Math.max(y1,y2);
      for(int i=smaller+1; i<larger; i++){
        if(!isEmpty(x1,i)){
          return false;
        }
      }
      return true;
    }
    if(y1-y2==0){
      int smaller = Math.min(x1,x2);
      int larger = Math.max(x1,x2);
      for(int i=smaller+1; i<larger; i++){
        if(!isEmpty(i,y1)){
          return false;
        }
      }
      return true;
    }
    return false;
  }
  static boolean moveBishop(int x1, int x2, int y1, int y2){
    if(Math.abs(x1-x2)==Math.abs(y1-y2)){
      int directionX = (x2-x1)/Math.abs(x2-x1);
      int directionY = (y2-y1)/Math.abs(y2-y1);

      int tempx = x1+directionX;
      int tempy = y1+directionY;
      while(tempx<x2){
        if(!isEmpty(tempx,tempy)){
          return false;
        }
        tempx+=directionX;
        tempy+=directionY;
      }
      return true;
    }
    return false;
  }

  
  static boolean isCapital(int x, int y){
    if(board[y][x]>='A'&&board[y][x]<='Z'){
      return true;
    }
    return false;
  }
  static boolean isLowerCase(int x, int y){
    if(board[y][x]>='a'&&board[y][x]<='z'){
      return true;
    }
    return false;
  }
  static boolean isEmpty(int x, int y){
    return board[y][x]=='_';
  }


  

  static void printError(int e){
    System.out.println(error[e]);
  }


  
}
