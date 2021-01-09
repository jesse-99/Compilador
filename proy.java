package AutoII;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;

import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Color;
public class proy {

	private JFrame frame;
	private JScrollPane scrollPane;
	JTextArea textArea; 
	JTable tabla;
	DefaultTableModel datos;
	int contadorLineas = 0,numl=1;
    String resultado = "";
	String vecTemp[],vecTokens[],tokenEnt[],pilaCima[];
	String palabra = "",valor;
	String patron, patronID;
	int accion[][],numero[][];
	private Stack<String> pila = new Stack<String>();
	private Stack<Integer> pilaSem = new Stack<Integer>();
	private Stack<String> pilaOpe = new Stack<String>();
	int IEstado=0,IToken=0,E=0,A=0;
	boolean bCorrecto=false;
	String tipod[]={"error","char","float","int"};//0error 1char 2float 3int
	int tipd=0,varAnt=0,posid=0;
	boolean decl=true;
	

public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
		public void run() {
			try {
				proy window = new proy();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});
}


public proy() {
	initialize();
}

private void initialize() {
		frame = new JFrame();
		
		frame.setBounds(100, 100, 600, 1000);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		textArea = new JTextArea();
		matricesSin();
	    datos = new DefaultTableModel();
		tabla = new JTable(datos);
		datos.addColumn("ELEMENTO");
		datos.addColumn("VALOR");
		datos.addColumn("SIGNIFICADO");
		datos.addColumn("CARACTER");
		scrollPane = new JScrollPane();
		scrollPane.setBounds(28,480,500,300);
		frame.getContentPane().add(scrollPane);
	
	JButton btnAnalizar = new JButton("CHECAR ANALISIS");
	btnAnalizar.setFont(new Font("Tahoma", Font.BOLD, 12));
	btnAnalizar.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			try {
				inicio();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	});
	btnAnalizar.setBounds(200, 21, 200, 53);
	frame.getContentPane().add(btnAnalizar);
	
	
	
	scrollPane_1 = new JScrollPane();
	scrollPane_1.setBounds(28, 85, 505, 350);
	frame.getContentPane().add(scrollPane_1);
	
	scrollPane_1.setViewportView(textArea);
	
	
}

public  void inicio() throws IOException{
	String cadenaE = "";		//agarra el contenido que le metemos
	String contenido = new String(textArea.getText());
	StringTokenizer st = new StringTokenizer(contenido, "\n");
	
	
	int x=0,numr=tabla.getRowCount();
	while(x<numr) {
	datos.removeRow(0);
	x++;
	}
	tabla.removeAll();
	pila.clear();    
	pila.push("I0");
	bCorrecto=false;
	IEstado=0;
	IToken=0;
	E=0;
	A=0;
	pilaSem.clear();
	pilaOpe.clear();
	
	
	while (st.hasMoreTokens()) {
		String linea = st.nextToken();
		if(st.hasMoreTokens()) 
			cadenaE = cadenaE + linea + " S@L ";
		else
			cadenaE = cadenaE + linea;
		contadorLineas = contadorLineas + 1; //CONTADOR PARA LINEAS
	}
	vecTemp = new String[1];
	vecTemp = cadenaE.split(" ");//DIVIDE LA CADENA Y LA METE A UN VECTOR
	
	String vecT2[]= new String[vecTemp.length]; //Para sintactico
	
	int j=0;
	for (int i = 0; i < vecTemp.length; i++) {
		palabra = vecTemp[i];
		if(!palabra.equals("")) {
		valor = analisislexico(palabra);
		if(valor.equals("ErrorLexico")) {
			JOptionPane.showMessageDialog(frame,"NO SE CUMPLEN PATRONES","ERROR LEXICO",JOptionPane.ERROR_MESSAGE);
		}else {
		vecT2[j]=valor;
		j++;
		if(valor.equals("S@L"))//SALTO DE LINEA
			numl++;
		   else { 					  
			   analisisSintactico();
		     }
		}
		}
	}
	vecTokens= new String[j];
	
	for (int i = 0; i < j; i++) {
		vecTokens[i]=vecT2[i];
	}
	if(vecTokens.length!=0)
	if(vecTokens[vecTokens.length-1].equals("$")) {
	}else {
		JOptionPane.showMessageDialog(frame,"FALTA $","ERROR ENTRADA INCORRECTA",JOptionPane.ERROR_MESSAGE);
		
	}
}

public void matricesSin(){
tokenEnt= new String[21];
pilaCima= new String[32];
accion= new int[32][21]; 
numero= new int[32][21]; 

tokenEnt[0]="id";
tokenEnt[1]="int";
tokenEnt[2]="float";
tokenEnt[3]="char";
tokenEnt[4]=",";
tokenEnt[5]=";";
tokenEnt[6]="+";
tokenEnt[7]="-";
tokenEnt[8]="*";
tokenEnt[9]="/";
tokenEnt[10]="(";
tokenEnt[11]=")";
tokenEnt[12]="=";
tokenEnt[13]="$";
tokenEnt[14]="P";
tokenEnt[15]="Tipo";
tokenEnt[16]="V";
tokenEnt[17]="A";
tokenEnt[18]="E";
tokenEnt[19]="T";
tokenEnt[20]="F";

pilaCima[0]="I0";
pilaCima[1]="I1";
pilaCima[2]="I2";
pilaCima[3]="I3";
pilaCima[4]="I4";
pilaCima[5]="I5";
pilaCima[6]="I6";
pilaCima[7]="I7";
pilaCima[8]="I8";
pilaCima[9]="I9";	
pilaCima[10]="I10";	
pilaCima[11]="I11";	
pilaCima[12]="I12";	
pilaCima[13]="I13";	
pilaCima[14]="I14";	
pilaCima[15]="I15";	
pilaCima[16]="I16";
pilaCima[17]="I17";
pilaCima[18]="I18";
pilaCima[19]="I19";
pilaCima[20]="I20";
pilaCima[21]="I21";
pilaCima[22]="I22";
pilaCima[23]="I23";
pilaCima[24]="I24";
pilaCima[25]="I25";
pilaCima[26]="I26";	
pilaCima[27]="I27";	
pilaCima[28]="I28";	
pilaCima[29]="I29";	
pilaCima[30]="I30";	
pilaCima[31]="I31";	

// 1 --> DESPLAZA, 2 -- >REDUCE
accion[0][0]=1; //I7 Desplaza
accion[0][1]=1;
accion[0][2]=1;
accion[0][3]=1;
accion[0][14]=1;
accion[0][15]=1;
accion[0][17]=1;

accion[1][13]=2; //P0 reduce

accion[2][0]=1;

accion[3][13]=2;

accion[4][0]=2;
accion[5][0]=2;
accion[6][0]=2;

accion[7][12]=1;

accion[8][4]=1;
accion[8][5]=1;
accion[8][16]=1;

accion[9][0]=1;
accion[9][10]=1;
accion[9][18]=1;
accion[9][19]=1;
accion[9][20]=1;

accion[10][13]=2;

accion[11][0]=1;

accion[12][0]=1;
accion[12][1]=1;
accion[12][2]=1;
accion[12][3]=1;
accion[12][14]=1;
accion[12][15]=1;
accion[12][17]=1;

accion[13][5]=1;
accion[13][6]=1;
accion[13][7]=1;

accion[14][5]=2;
accion[14][6]=2;
accion[14][7]=2;
accion[14][8]=1;
accion[14][9]=1;
accion[14][11]=2;

accion[15][5]=2;
accion[15][6]=2;
accion[15][7]=2;
accion[15][8]=2;
accion[15][9]=2;
accion[15][11]=2;

accion[16][0]=1;
accion[16][10]=1;
accion[16][18]=1;
accion[16][19]=1;
accion[16][20]=1;

accion[17][5]=2;
accion[17][6]=2;
accion[17][7]=2;
accion[17][8]=2;
accion[17][9]=2;
accion[17][11]=2;

accion[18][4]=1;
accion[18][5]=1;
accion[18][16]=1;

accion[19][13]=2;

accion[20][13]=2;

accion[21][0]=1;
accion[21][10]=1;
accion[21][19]=1;
accion[21][20]=1;

accion[22][0]=1;
accion[22][10]=1;
accion[22][19]=1;
accion[22][20]=1;

accion[23][0]=1;
accion[23][10]=1;
accion[23][20]=1;

accion[24][0]=1;
accion[24][10]=1;
accion[24][20]=1;

accion[25][6]=1;
accion[25][7]=1;
accion[25][11]=1;

accion[26][13]=2;

accion[27][5]=2;
accion[27][6]=2;
accion[27][7]=2;
accion[27][8]=1;
accion[27][9]=1;
accion[27][11]=2;

accion[28][5]=2;
accion[28][6]=2;
accion[28][7]=2;
accion[28][8]=1;
accion[28][9]=1;
accion[28][11]=2;

accion[29][5]=2;
accion[29][6]=2;
accion[29][7]=2;
accion[29][8]=2;
accion[29][9]=2;
accion[29][11]=2;

accion[30][5]=2;
accion[30][6]=2;
accion[30][7]=2;
accion[30][8]=2;
accion[30][9]=2;
accion[30][11]=2;

accion[31][5]=2;
accion[31][6]=2;
accion[31][7]=2;
accion[31][8]=2;
accion[31][9]=2;
accion[31][11]=2;

numero[0][0]=7; // este 7 es: I7
numero[0][1]=4;
numero[0][2]=5;
numero[0][3]=6;
numero[0][14]=1;
numero[0][15]=2;
numero[0][17]=3;

numero[1][13]=0;//Este es de P0

numero[2][0]=8;

numero[3][13]=2;

numero[4][0]=3;
numero[5][0]=4;
numero[6][0]=5;

numero[7][12]=9;

numero[8][4]=11;
numero[8][5]=12;
numero[8][16]=10;

numero[9][0]=17;
numero[9][10]=16;
numero[9][18]=13;
numero[9][19]=14;
numero[9][20]=15;

numero[10][13]=1;

numero[11][0]=18;

numero[12][0]=7;
numero[12][1]=4;
numero[12][2]=5;
numero[12][3]=6;
numero[12][14]=19;
numero[12][15]=2;
numero[12][17]=3;

numero[13][5]=20;
numero[13][6]=21;
numero[13][7]=22;

numero[14][5]=11;
numero[14][6]=11;
numero[14][7]=11;
numero[14][8]=23;
numero[14][9]=24;
numero[14][11]=11;

numero[15][5]=14;
numero[15][6]=14;
numero[15][7]=14;
numero[15][8]=14;
numero[15][9]=14;
numero[15][11]=14;

numero[16][0]=17;
numero[16][10]=16;
numero[16][18]=25;
numero[16][19]=14;
numero[16][20]=15;

numero[17][5]=16;
numero[17][6]=16;
numero[17][7]=16;
numero[17][8]=16;
numero[17][9]=16;
numero[17][11]=16;

numero[18][4]=11;
numero[18][5]=12;
numero[18][16]=26;

numero[19][13]=7;

numero[20][13]=8;

numero[21][0]=17;
numero[21][10]=16;
numero[21][19]=27;
numero[21][20]=15;

numero[22][0]=17;
numero[22][10]=16;
numero[22][19]=28;
numero[22][20]=15;

numero[23][0]=17;
numero[23][10]=16;
numero[23][20]=29;

numero[24][0]=17;
numero[24][10]=16;
numero[24][20]=30;

numero[25][6]=21;
numero[25][7]=22;
numero[25][11]=31;

numero[26][13]=6;

numero[27][5]=9;
numero[27][6]=9;
numero[27][7]=9;
numero[27][8]=23;
numero[27][9]=24;
numero[27][11]=9;

numero[28][5]=10;
numero[28][6]=10;
numero[28][7]=10;
numero[28][8]=23;
numero[28][9]=24;
numero[28][11]=10;

numero[29][5]=12;
numero[29][6]=12;
numero[29][7]=12;
numero[29][8]=12;
numero[29][9]=12;
numero[29][11]=12;

numero[30][5]=13;
numero[30][6]=13;
numero[30][7]=13;
numero[30][8]=13;
numero[30][9]=13;
numero[30][11]=13;

numero[31][5]=15;
numero[31][6]=15;
numero[31][7]=15;
numero[31][8]=15;
numero[31][9]=15;
numero[31][11]=15;

}

public String analisislexico(String valor) { //Aqui inicia el programa 
//lo primero que hace es analizar el valor que se ingreso
	String temp[] = new String[4];
	if(valor.equals("S@L")) {
		resultado = "S@L";
	}else
	if (valor.equals("+") ) {
		
		temp[0] = valor; 
		temp[1] = valor; 		//depende el valor ya sea +,-,/,*,(, manda a imprimir a su tabla
		temp[2] = "MAS"; 
		temp[3] = "NO CARACTER "; 
		datos.addRow(temp);
		scrollPane.setViewportView(tabla);
		resultado = temp[0];
	}else 
		if (valor.equals("-") ) {
			temp[0] = valor;
			temp[1] = valor;
			temp[2] = "MENOS";
			temp[3] = "NO CARACTER";
			datos.addRow(temp);
			scrollPane.setViewportView(tabla);
			resultado = temp[0];
	}else 
		if (valor.equals("*") ) {
			temp[0] = valor;
			temp[1] = valor;
			temp[2] = "POR";
			temp[3] = "NO CARACTER ";
			datos.addRow(temp);
			scrollPane.setViewportView(tabla);
			resultado = temp[0];
		}else
			if (valor.equals("/") ) {
				temp[0] = valor;
				temp[1] = valor;
				temp[2] = "ENTRE";
				temp[3] = "NO CARACTER ";
				datos.addRow(temp);
				scrollPane.setViewportView(tabla);
				resultado = temp[0];
			}else
				if (valor.equals("(") ) {
					temp[0] = valor;
					temp[1] = valor;
					temp[2] = "PARENTESIS QUE ABRE";
					temp[3] = "NO CARACTER ";
					datos.addRow(temp);
					scrollPane.setViewportView(tabla);
					resultado = temp[0];
				}else
					if (valor.equals(")") ) {
						temp[0] = valor;
						temp[1] = valor;
						temp[2] = "PARENTESIS QUE CIERRA";
						temp[3] = " NO CARACTER";
						datos.addRow(temp);
						scrollPane.setViewportView(tabla);
						resultado = temp[0];
					}else
						if (valor.equals(";") ) {
							temp[0] = valor;
							temp[1] = valor;
							temp[2] = "PUNTO Y COMA";
							temp[3] = " NO CARACTER";
							datos.addRow(temp);
							scrollPane.setViewportView(tabla);
							resultado = temp[0];
						}else
							if (valor.equals(",") ) {
								temp[0] = valor;
								temp[1] = valor;
								temp[2] = "COMA";
								temp[3] = "NO CARACTER ";
								datos.addRow(temp);
								scrollPane.setViewportView(tabla);
								resultado = temp[0];
							}else
								if (valor.equals("=") ) {
									temp[0] = valor;
									temp[1] = valor;
									temp[2] = "SIGNO IGUAL";
									temp[3] = "NO CARACTER ";
									datos.addRow(temp);
									scrollPane.setViewportView(tabla);
									resultado = temp[0];
								}else
									if (valor.equals("$") ) {
										temp[0] = valor;
										temp[1] = valor;
										temp[2] = "SIMBOLO DE PESOS";
										temp[3] = "NO CARACTER ";
										datos.addRow(temp);
										scrollPane.setViewportView(tabla);
										resultado = temp[0];
									}else
										if(valor.equals("int")| valor.equals("float")| valor.equals("char")) {
											temp[0] = valor;
											temp[1] = valor;
											temp[2] = "PR";
											temp[3] = "NO CARACTER ";
											datos.addRow(temp);
											scrollPane.setViewportView(tabla);
											resultado = temp[0];
											 if(valor.equals("int"))
												 tipd=3;
											 else
												 if(valor.equals("char"))
													 tipd=1;          //esto es para hacer operaciones semanticas despues
												 else
													 if(valor.equals("float"))
														 tipd=2;
										}else {
											patronID = "^([A-Z]|[a-z]|[_])([A-Z]*[a-z]*[_]*[0-9]*)*$";
											Pattern p = Pattern.compile(patronID);
											java.util.regex.Matcher reg = p.matcher(valor);
		
											if (reg.matches()) {
												temp[0] = "id";
												temp[1] = valor;
												temp[2] = "IDENTIFICADOR";
												temp[3] = tipod[tipd];
		
												boolean re=this.revisar(temp[1]); //aqui checa que no tenga dos identificadores que se llamen igual
												if(tabla.getRowCount()==0){
													datos.addRow(temp);
													scrollPane.setViewportView(tabla);
													resultado = temp[0];
												}else{  //este proceso verifica que la variable este declarada
														if(re==false){													
															String ant=tabla.getValueAt(tabla.getRowCount()-1, 1).toString();
															if(!(ant.equals("int")|ant.equals("float")|ant.equals("char")|ant.equals(","))) {
												
																JOptionPane.showMessageDialog(frame,"VARIABLE NO DECLARADA","ERROR SEMANTICO",JOptionPane.ERROR_MESSAGE);
																System.exit(0);
														
															}else {
																datos.addRow(temp);
																scrollPane.setViewportView(tabla);
																resultado = temp[0];
																
															}
															
														}else{
															resultado =idpos;
															
															if(tabla.getValueAt(posid, 3).toString().equals("int"))
																tipd=3;//ENTERO
															else
																if(tabla.getValueAt(posid, 3).toString().equals("float"))
																	tipd=2;//FLOTANTE
																else 
																	if(tabla.getValueAt(posid, 3).toString().equals("char"))
																        tipd=1;//CARACTER
															//aqui vuelve a checar el tipo que le corresponde y mandamos a imprimir a la pila semantica
															
															pilaSem.push(tipd);
															System.out.println(pilaSem);
															
															
														}
												}
										
											} 
											else {
													resultado = "ErrorLexico";
												}
										}
		
											
									
	

	return resultado;
}

String idpos="";

private JScrollPane scrollPane_1;
//aqui es el analisis sintactico 
//se ocupa un estado y una accion 
public void analisisSintactico() {
/// valor es nuestro token entrada
do {
encT();
E=numero[IEstado][IToken]; //las matrices estan cradas hasta arriba
A=accion[IEstado][IToken];

System.out.println(pila);

//Aqui agrega 
if (A==1) {///ACCION DE DESPLAZAR
pila.push(valor);
pila.push("I"+E);
IEstado=E;
if(valor.equals("(")|valor.equals("=")|valor.equals("+")|valor.equals("-")|valor.equals("/")|valor.equals("*")) {
	pilaOpe.push(valor); //SEMANTICO agrega a la pila lo que sea necesario 
	System.out.println(pilaOpe);
}
}else
if (A==2){///ACCION DE REDUCE
	Produccion();

}else {
JOptionPane.showMessageDialog(frame,"ERROR NO SE CUMPLE CON LA GRAMATICA ","ERROR SINTACTICO",JOptionPane.ERROR_MESSAGE);
System.exit(0);
}
}while(A==2 && bCorrecto==false);

}
public void encT() {
for (int j = 0; j < tokenEnt.length; j++) {
if(valor.equals(tokenEnt[j])) {
	IToken=j;
	break;
}	
}	

}

public void encE(String estant) {
for (int j = 0; j < pilaCima.length; j++) {
if(estant.equals(pilaCima[j])) {
	IEstado=j;
	break;
}	
}	
}
public void sacaPsem() {
int dato1,dato2; String op;

dato1=pilaSem.lastElement();
pilaSem.pop();
dato2=pilaSem.lastElement();
pilaSem.pop();
op=pilaOpe.lastElement();
pilaOpe.pop();

if(op.equals("(")) {
op=pilaOpe.lastElement();
		pilaOpe.pop();
}

if(op.equals("=")) { // 1 es char, 2 float, 3 int
if(dato2==1) {
	if(dato1==2) {
		JOptionPane.showMessageDialog(frame,"LA OPERACION NO SE PUEDE REALIZAR","ERROR SEMANTICO ",JOptionPane.ERROR_MESSAGE);
		System.exit(0);//char es igual a float da error 
	}else
		pilaSem.push(1);//aqui agrega si char es igual a char o char es igual a int y da como resultado un char
	
}else
	if(dato2==2) {
		if(dato1==1) {
			JOptionPane.showMessageDialog(frame,"ERROR LA OPERACION NO SE PUEDE REALIZAR ","ERROR SEMANTICO",JOptionPane.ERROR_MESSAGE);
			System.exit(0);//un float igual a char da error
		}else
			pilaSem.push(2);//float igual a float o float igual a int da como resultado un float
	}else
		if(dato2==3) {
			if(dato1==1) {
				JOptionPane.showMessageDialog(frame,"ERROR LA OPERACION NO SE PUEDE REALIZAR","ERROR SEMANTICO",JOptionPane.ERROR_MESSAGE);
				System.exit(0);//int igual a char da error
			}else
				pilaSem.push(3);// int igual a float o int igual a int da como resultado un int
		}
}else
if(!op.equals("(")) {
	if(dato2==1) {
			JOptionPane.showMessageDialog(frame,"ERROR LA OPERACION NO SE PUEDE REALIZAR","ERROR SEMANTICO",JOptionPane.ERROR_MESSAGE);
			System.exit(0);				
	}else
		if(dato2==2) {
			if(dato1==1) {
				JOptionPane.showMessageDialog(frame,"ERROR LA OPERACION NO SE PUEDE REALIZAR","ERROR SEMANTICO",JOptionPane.ERROR_MESSAGE);
				System.exit(0);//un float no se puede multiplicar ni dividir ni sumar ni restar con un char
			}else
				pilaSem.push(2);//un float puede multiplicar dividir sumar y restar ya sea con un float o un int y da como resultado un float
		}else
			if(dato2==3) {
				if(dato1==1) {
					JOptionPane.showMessageDialog(frame,"ERROR LA OPERACION NO SE PUEDE REALIZAR","ERROR SEMANTICO",JOptionPane.ERROR_MESSAGE);
					System.exit(0);//un int no se puede mult div sumar ni restar con un char
				}else
					if(dato1==2) {
						pilaSem.push(2);//un int se puede mult div sumar restar con un float y da como resultado un float
					}else
						if(dato1==3) {
						pilaSem.push(3);//un int se puede mult div sumar restar con un int y da como resultado un int 
						}
			}
}
System.out.println(pilaSem);

}
public void Produccion() {
String estant;
switch (E) { //P0.....P16
case 0:
bCorrecto=true;
JOptionPane.showMessageDialog(frame,"ENTRADA CORRECTA","CORRECTO FINALIZADO",JOptionPane.INFORMATION_MESSAGE);
break;
case 1: //quita tipo id v y sus estados por eso es el doble
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
estant = pila.lastElement();
encE(estant);
pila.push("P"); //agrega P
IEstado=numero[IEstado][14];
pila.push("I"+IEstado);

break;
case 2:
pila.pop();
pila.pop();
estant = pila.lastElement();
encE(estant);
pila.push("P");
IEstado=numero[IEstado][14];
pila.push("I"+IEstado);

break;
case 3:
pila.pop();
pila.pop();
estant = pila.lastElement();
encE(estant);
pila.push("Tipo");
IEstado=numero[IEstado][15];
pila.push("I"+IEstado);

break;
case 4:
pila.pop();
pila.pop();
estant = pila.lastElement();
encE(estant);
pila.push("Tipo");
IEstado=numero[IEstado][15];
pila.push("I"+IEstado);

break;
case 5:
pila.pop();
pila.pop();
estant = pila.lastElement();
encE(estant);
pila.push("Tipo");
IEstado=numero[IEstado][15];
pila.push("I"+IEstado);

break;
case 6:
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
estant = pila.lastElement();
encE(estant);
pila.push("V");
IEstado=numero[IEstado][16];
pila.push("I"+IEstado);

break;
case 7:
pila.pop();
pila.pop();
pila.pop();
pila.pop();
estant = pila.lastElement();
encE(estant);
pila.push("V");
IEstado=numero[IEstado][16];
pila.push("I"+IEstado);

break;
case 8:
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
estant = pila.lastElement();
encE(estant);
pila.push("A");
IEstado=numero[IEstado][17];
pila.push("I"+IEstado);
sacaPsem(); //aqui es donde se realiza las operaciones con las pilas semnaticas

break;
case 9:
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
estant = pila.lastElement();
encE(estant);
pila.push("E");
IEstado=numero[IEstado][18];
pila.push("I"+IEstado);
sacaPsem();	
break;
case 10:
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
estant = pila.lastElement();
encE(estant);
pila.push("E");
IEstado=numero[IEstado][18];
pila.push("I"+IEstado);
sacaPsem();
break;
case 11:
pila.pop();
pila.pop();
estant = pila.lastElement();
encE(estant);
pila.push("E");
IEstado=numero[IEstado][18];
pila.push("I"+IEstado);

break;
case 12:
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
estant = pila.lastElement();
encE(estant);
pila.push("T");
IEstado=numero[IEstado][19];
pila.push("I"+IEstado);
sacaPsem();

break;
case 13:
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
estant = pila.lastElement();
encE(estant);
pila.push("T");
IEstado=numero[IEstado][19];
pila.push("I"+IEstado);
sacaPsem();
break;
case 14:
pila.pop();
pila.pop();
estant = pila.lastElement();
encE(estant);
pila.push("T");
IEstado=numero[IEstado][19];
pila.push("I"+IEstado);


break;
case 15:
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
pila.pop();
estant = pila.lastElement();
encE(estant);
pila.push("F");
IEstado=numero[IEstado][20];
pila.push("I"+IEstado);

break;
case 16:
pila.pop();
pila.pop();
estant = pila.lastElement();
encE(estant);
pila.push("F");
IEstado=numero[IEstado][20];
pila.push("I"+IEstado);

break;
default:
break;
}

}
public boolean revisar(String valor){
	boolean re=true;

	for (int i = 0; i < tabla.getRowCount(); i++) {
	if(tabla.getValueAt(i, 1).toString().equals(valor)){
	       re=true;
	       idpos=tabla.getValueAt(i, 0).toString();
	       posid=i;
	       return re;
	}else{ if(!tabla.getValueAt(i, 1).toString().equals(valor)){
		re=false;
	}
	}
	}
	return re;
}


}

