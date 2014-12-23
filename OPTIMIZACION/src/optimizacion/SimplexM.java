/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package optimizacion;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


public class SimplexM {

    private int numVariables, numRestricciones,variables,restricciones;
    private int contadorVariableHolgura, contadorVariableSuperflua, contadorVariableFicticia,
            contadoriteraciones, bandera;
    private float valorM=1000000;
    private ArrayList<Float> funcionObjetivo;
    private Object []espacio, espacio2;    
    private String iteracion="Iteracion";
    private boolean maximizar;
    TableModel modelo;
    JTable tabla=new JTable();
    JTable tablaSalida=new JTable();
    
    
    public SimplexM(TableModel modeloLLega,int Llvariables,int Llrestricciones) {
        
        variables=numVariables=Llvariables;  //Toca ingresar despues el numero de variables y restricciones dinamicamente.CAMBIAR EL NÚMERO DE VARIABLES Y RESTRICCIONES!!!!
        restricciones=numRestricciones=Llrestricciones;
        contadorVariableHolgura = 0;
        contadorVariableSuperflua = 0;
        contadorVariableFicticia = 0;
        funcionObjetivo = new ArrayList<Float>();
        contadoriteraciones=1;
        bandera=0;
        maximizar=false;
        this.modelo=modeloLLega;
        tabla.setModel(modelo);
                
//        //Tabla de Prueba de 5 columnas, 7 filas.
//        String[] columnas= {"","X1","X2","X3","<=,>=,=","valor restriccion"}; //CAMBIAR EL NÚMERO DE VARIABLES Y RESTRICCIONES!!!!
//        String[][]filas= {
//            //jueguen con estos valores ojala basados en uno tomado de tora
//            {"nombre variable",     "v1",        "v2",      "v3",           " ",       " "},
//            {"Z max"/*o Z min*/,      "-125",       "50",     "-320",         " ",       " "}, 
//            {"Restriccion 1",       "14",         "11",     "10" ,          ">=",      "12"},
//            {"Restriccion 2",       "-32",        "2",      "-25",          "<=",      "32"},
//            {"Restriccion 3",       "1",         "3",       "7",            ">=",       "1"},
//            {"Restriccion 4",       "-4",         "45",     "8",            ">=",       "12"},
//            {"Restriccion 5",       "5",         "5",       "-6",           "=",       "14"},
//            {"valor min",           "0",         "0",       "0" ,           " ",       " "},
//            {"Valor max",        "infinito",  "infinito",   "infinito",     " ",       " "}     
//        };
//        
//        DefaultTableModel modelo=new DefaultTableModel(filas,columnas);
//        tabla.setModel(modelo);
        
        Resolver();
   }
    
    public void Resolver()
    {
        //Restableciendo los valores de los contadores.
        contadorVariableHolgura = 0;
        contadorVariableSuperflua = 0;
        contadorVariableFicticia = 0;
        contadoriteraciones = 1;
        bandera=0;
        
        //Restableciendo la Nueva funcion Objetivo.
        funcionObjetivo.clear();
        if(tabla.getValueAt(1, 0).equals("Z max")){
            maximizar=true;
        }
        inicializarNuevaFuncionObjetivo();
        
        //Creando el modelo de la Tabla de Salida.
        DefaultTableModel modelo= new DefaultTableModel();//tablaSalida= new JTable(modelo);
        
        //Añadiendo la columna y nombres para las n variables BÁSICAS.
        modelo.addColumn("BÁSICAS");
        for(int i=1 ; i<numVariables+1; i++){
            modelo.addColumn("X" + i);
        }
       
        //Añadiendo la primera fila para la función objetivo.
        Object[] filaFuncionObjetivo = new Object[modelo.getColumnCount()];
        if(maximizar){
          filaFuncionObjetivo[0] = "Zmax"; //Este es el nombre de la fila.
        }
        else{
            filaFuncionObjetivo[0] = "Zmin"; //Este es el nombre de la fila.
        }
        
        for(int i=1 ; i<filaFuncionObjetivo.length ; i++){
            filaFuncionObjetivo[i] = new Float(0);
        }
        modelo.addRow(filaFuncionObjetivo);
        
        //Añadiendo las columnas y filas de las nuevas variables según el tipo de restricción.
        for(int fila=2; fila<tabla.getRowCount()-2; fila++){
            TIPO_RESTRICCION:
            for(int columna=tabla.getColumnCount()-2 ; columna<tabla.getColumnCount()-1; columna++){
                String tipoRestriccion = (String) tabla.getValueAt(fila, columna);
                switch(tipoRestriccion){
                    case ">=":
                        DefaultTableModel modeloActualizado = restriccionMayorOIgual(modelo);
                        modelo = modeloActualizado;
                        //Despejando la enésima variable ficticia.
                        ArrayList<Float> ecuacionDespejada = despejarVariableFicticia(tipoRestriccion, fila);
                        //Enviando al método para llenar la nueva función objetivo.
                        llenarTablaConNuevaFuncionObjetivo(ecuacionDespejada);
                        break TIPO_RESTRICCION;
                    
                   case "<=":
                        DefaultTableModel modeloActualizado1 = restriccionMenorOIgual(modelo);
                        modelo = modeloActualizado1;
                        break TIPO_RESTRICCION;
                    
                    default:
                        DefaultTableModel modeloActualizado2 = restriccionIgual(modelo);
                        modelo = modeloActualizado2;
                        //Despejando la enésima variable ficticia.
                        ArrayList<Float> ecuacionDespejada2 = despejarVariableFicticia(tipoRestriccion, fila);
                        //Enviando al método para llenar la nueva función objetivo.
                        llenarTablaConNuevaFuncionObjetivo(ecuacionDespejada2);
                        break TIPO_RESTRICCION;
                }
            }
            
        }
       
        //Añadiendo y llenando la columna SOLUCIÓN a partir de la tabla de valores recibida.
        modelo.addColumn("SOLUCIÓN");
        for(int fila=2; fila<tabla.getRowCount()-2; fila++){
            String valor = (String)tabla.getValueAt(fila, tabla.getColumnCount()-1);
            modelo.setValueAt(Float.parseFloat(valor), (fila-1), modelo.getColumnCount()-1);
        }
        
        //Añadiendo ceros en las celdas vacías.
        for(int fila=0; fila<modelo.getRowCount(); fila++){
            for(int columna=1; columna<modelo.getColumnCount(); columna++){
                Object valor = modelo.getValueAt(fila, columna);
                if(valor == null){
                    modelo.setValueAt(new Float(0), fila, columna);
                }
            }
        }
        
        //Añadiendo al modelo de la tabla de salida los valores de los coeficientes de las 
        //n variables de decisión para cada una de las m restricciones,
        //a partir de la tabla de valores recibida.
        for(int fila=2 ; fila<tabla.getRowCount()-2; fila++){
            for(int columna=1 ; columna<tabla.getColumnCount()-2 ; columna++){
                String valor = (String)tabla.getValueAt(fila, columna);
                modelo.setValueAt(Float.parseFloat(valor), (fila-1), columna);
            }
        }
        
        //Añadiendo los valores de los coeficientes de la Nueva F. Objetivo.
        for(int columna=1; columna<=numVariables; columna++){
            modelo.setValueAt(funcionObjetivo.get(columna), 0, columna);
        }
        //El siguiente es el valor del término independiente ubicado en la columna SOLUCIÓN..
        modelo.setValueAt(funcionObjetivo.get(funcionObjetivo.size()-1), 0, modelo.getColumnCount()-1);
        
       //Añadiendo la columna de Constante/entrante para determinar la fila que sale.
        Object [] columna=new Object[modelo.getRowCount()];
        
       for(int i=0;i<columna.length;i++){
        columna[i]=new Float(0);
       }        
      
       modelo.addColumn("Constante/entrada",columna);
       
       //Llamando el método proceso.
       if(maximizar){
           procesoUno(modelo);
       }
       else{
           procesoDos(modelo);
       }
    
    }
     public void inicializarNuevaFuncionObjetivo(){
        //Inicializando el vector de la nueva función objetivo y llenando con los valores iniciales.
        funcionObjetivo.add(new Float(1)); //Esta es la posición del vector para el coeficiente de Z que siempre es 1.
        //Añadiendo y llenando las posiciones del vector para las n variables de decisión.
        for(int columna=1 ; columna<=numVariables; columna++){
            String valor = (String) tabla.getValueAt(1, columna);
            funcionObjetivo.add(Float.parseFloat(valor)*-1);//Por -1 porque pasan a restar. 
        }
        //Añadiendo y llenando la posición para el término independiente.
        funcionObjetivo.add(new Float(0));
        //Vector Inicializado.
    }
   
    
    //  Método para las restricciones >=, se les debe sumar una variable ficticia(F1) y restar una Superflua(S1)
    public DefaultTableModel restriccionMayorOIgual(DefaultTableModel modelo){
        //Añadiendo la nueva columna para la variable superflua.
        contadorVariableSuperflua++;
        modelo.addColumn("S" + contadorVariableSuperflua);
        //Añadiendo el valor M a la fila de la F.Objetivo y Columna S. Nota=Debe ser -M cuando sea MINIMIZAR.
        if(maximizar){
            modelo.setValueAt(valorM, 0, modelo.getColumnCount()-1);
        }
        else{
            modelo.setValueAt((-1*valorM), 0, modelo.getColumnCount()-1);
        }
        //Añadiendo la nueva columna para la variable superflua.
        contadorVariableFicticia++;
        modelo.addColumn("F" + contadorVariableFicticia);
        
        //Añadiendo la fila de la nueva variable BÁSICA  a la tabla de salida.
        Object[] nuevaFilaVariableBasica = new Object[modelo.getColumnCount()];
        nuevaFilaVariableBasica[0] = "F" + contadorVariableFicticia;//Este es el nombre de la fila.
        for(int i=1 ; i<nuevaFilaVariableBasica.length-2 ; i++){
            nuevaFilaVariableBasica[i] = new Float(0);
        }
        nuevaFilaVariableBasica[nuevaFilaVariableBasica.length-2]= new Float(-1);
        nuevaFilaVariableBasica[nuevaFilaVariableBasica.length-1]= new Float(1);
        modelo.addRow(nuevaFilaVariableBasica);
        
        //Devolviendo el modelo.
        return modelo;
    }
    
    //  Método para las restricciones <=, se les debe sumar una variable de holgura(H1)
    public DefaultTableModel restriccionMenorOIgual(DefaultTableModel modelo){
        //Añadiendo la nueva columna para la variable de holgura.
        contadorVariableHolgura++;
        modelo.addColumn("H" + contadorVariableHolgura);
                
        //Añadiendo la fila de la nueva variable BÁSICA  a la tabla de salida.
        Object[] nuevaFilaVariableBasica = new Object[modelo.getColumnCount()];
        nuevaFilaVariableBasica[0] = "H" + contadorVariableHolgura;//Este es el nombre de la fila.
        for(int i=1 ; i<nuevaFilaVariableBasica.length-1 ; i++){
            nuevaFilaVariableBasica[i] = new Float(0);
        }
        nuevaFilaVariableBasica[nuevaFilaVariableBasica.length-1]= new Float(1);
        modelo.addRow(nuevaFilaVariableBasica);
        
        //Devolviendo el modelo.
        return modelo;
    }
    
    //  Método para las restricciones =, se les debe sumar una variable ficticia(F1).
    public DefaultTableModel restriccionIgual(DefaultTableModel modelo){
        //Añadiendo la nueva columna para la variable ficticia.
        contadorVariableFicticia++;
        modelo.addColumn("F" + contadorVariableFicticia);
        //Añadiendo la fila de la nueva variable BÁSICA  a la tabla de salida.
        Object[] nuevaFilaVariableBasica = new Object[modelo.getColumnCount()];
        nuevaFilaVariableBasica[0] = "F" + contadorVariableFicticia;//Este es el nombre de la fila.
        for(int i=1 ; i<nuevaFilaVariableBasica.length-1 ; i++){
            nuevaFilaVariableBasica[i] = new Float(0);
        }
        nuevaFilaVariableBasica[nuevaFilaVariableBasica.length-1]= new Float(1);
        modelo.addRow(nuevaFilaVariableBasica);
        
        //Devolviendo el modelo.
        return modelo;
     }
    
    public ArrayList despejarVariableFicticia(String tipoRestriccion, int numRestriccion){
        //Creando la ecuación de despeje de la variable ficticia.
        ArrayList ecuacion = new ArrayList<Float>();
        ecuacion.add(new Float(1));
        
        //Preguntando si se trata de una ecuación de tipo >= Ó =.
        if(tipoRestriccion.equals(">=")){
            ecuacion.add(new Float(1)); //Sumando valor de S siempre positivo.
            //Sumando los coeficientes de las n variables de decisión con signo contrario. Sumando además
            //el término independiente con mismo signo.
            for(int columna=1 ; columna<tabla.getColumnCount(); columna++){
               if(columna != tabla.getColumnCount()-2){ //No queremos el tipo de restricción.
                   if (columna == tabla.getColumnCount()-1){ //Columna del término independiente.
                       String valor = (String) tabla.getValueAt(numRestriccion, columna) ;
                       ecuacion.add(Float.parseFloat(valor)); //Sumando el término independiente con mismo signo.
                   }
                   else{
                      String valor = (String) tabla.getValueAt(numRestriccion, columna) ;
                      ecuacion.add((Float.parseFloat(valor)*-1)); //Cambio de signo.
                   }
               }
            }
            //Imprimiendo la ecuación despejada para efectos de prueba.
            imprimirEcuacionDespejada(ecuacion); 
            //Devolviendo la ecuación.
            return ecuacion;
        }
        else{
            ecuacion.add(new Float(0)); //Sumando valor de S siempre positivo.
            //Sumando los coeficientes de las n variables de decisión con signo contrario. Sumando además
            //el término independiente con mismo signo.
            for(int columna=1 ; columna<tabla.getColumnCount(); columna++){
               if(columna != tabla.getColumnCount()-2){ //No queremos el tipo de restricción.
                   if (columna == tabla.getColumnCount()-1){ //Columna del término independiente.
                       String valor = (String) tabla.getValueAt(numRestriccion, columna) ;
                       ecuacion.add(Float.parseFloat(valor)); //Sumando el término independiente con mismo signo.
                   }
                   else{
                      String valor = (String) tabla.getValueAt(numRestriccion, columna) ;
                      ecuacion.add((Float.parseFloat(valor)*-1)); //Cambio de signo.
                   }
               }
            }
            //Imprimiendo la ecuación despejada para efectos de prueba.
            imprimirEcuacionDespejada(ecuacion);
            //Devolviendo la ecuación.
            return ecuacion;
        }
   }
    
    public void llenarTablaConNuevaFuncionObjetivo(ArrayList ecuacion){
        // System.out.println("\nTamaño vector ecuación = "+ecuacion.size());
        //System.out.println("Tamaño vector F. Objetivo = "+funcionObjetivo.size());
       
        //Multiplicando por M la ecuación despejada recibida y sumando en la posición exacta del vector de la F. Objetivo. 
        for( int i=2 ; i<ecuacion.size() ; i++){
           if(i != ecuacion.size()-1){
               if(maximizar){
                    Float valor = (Float) ecuacion.get(i)*valorM; //Por +M porque pasan a sumar. NOTA= Solo para (Zmáx)
                    Float nuevoValor = funcionObjetivo.get(i-1) + valor; //Sumando en la posición exacta.
                    funcionObjetivo.set(i-1, nuevoValor);//Asignando el nuevo valor.
               }
               else{
                   Float valor = (Float) ecuacion.get(i)*valorM*-1; //Por -M porque pasan a restar. NOTA= Solo para (Zmin)
                   Float nuevoValor = funcionObjetivo.get(i-1) + valor; //Sumando en la posición exacta.
                   funcionObjetivo.set(i-1, nuevoValor);//Asignando el nuevo valor.
               }
          }
          else{
               //System.out.println("\n"+i);
               if(maximizar){
                    Float valor = (Float) ecuacion.get(i)*valorM*-1; //Por -M por ser el término independiente. NOTA= Solo para (Zmáx)
                    Float nuevoValor = funcionObjetivo.get(i-1) + valor;
                    funcionObjetivo.set(i-1, nuevoValor);
               }
               else{
                   Float valor = (Float) ecuacion.get(i)*valorM; //Por +M por ser el término independiente. NOTA= Solo para (Zmin)
                   Float nuevoValor = funcionObjetivo.get(i-1) + valor;
                   funcionObjetivo.set(i-1, nuevoValor);
               }
          }
        }
        
        //Imprimiendo el vector de la nueva F. Objetivo para efectos de prueba.
        System.out.println();
        System.out.print("Vector Nueva F. Objetivo = ");
        Iterator iterador = funcionObjetivo.iterator(); 
          while ( iterador.hasNext() ) { 
             Float termino = (Float)iterador.next(); 
             System.out.print(termino+ "  "); 
          }
        System.out.println();  
    }
    
        
    //Método Imprimir ecuación despejada para efectos de prueba.
    public void imprimirEcuacionDespejada(ArrayList ecuacion){
        System.out.println();
        for(int i=0 ; i<ecuacion.size() ; i++){
                if (i==0){
                   System.out.print("F" + contadorVariableFicticia + " = "); 
                }
                else if(i==1){
                    if ((Float)ecuacion.get(i) != 0){
                        System.out.print(ecuacion.get(i)+ "S" + contadorVariableSuperflua + " ");
                    }
                }
                else if(i==(ecuacion.size()-1)){
                  if((Float)ecuacion.get(i) > new Float(0)){
                      System.out.print("+"+ecuacion.get(i));
                  }
                  else{
                     System.out.print(ecuacion.get(i) + " ");
                  }
                }
                else{ 
                  if((Float)ecuacion.get(i) > new Float(0)){
                     System.out.print("+" + ecuacion.get(i) + "X" + (i-1) + " ");
                  }
                  else{
                      System.out.print(ecuacion.get(i) + "X" + (i-1) + " ");
                  } 
                }
        }
   }
    
    
    public void procesoUno(DefaultTableModel procesado){
        //Realizando la primera iteración del método.
        Float negativo=new Float(0);
        //Preguntando por la COLUMNA que entra.
        int col=0;
        for(int i=1;i<procesado.getColumnCount()-3;i++){
            if(((Float)procesado.getValueAt(0, i))<new Float(0)){//se pregunta por el valor mas negativo de la matriz con los datos
                if(((Float)procesado.getValueAt(0, i))<negativo){
                    negativo=((Float)procesado.getValueAt(0, i));
                    col=i;
                }
            }
        }
        System.out.println("\nNumero mas negativo de Z= " +negativo+ "\nCOLUMNA que entra= " +col);
        
        //Preguntando por la FILA que sale.
        Float resultados[] = new Float[restricciones];
        for (int i=1;i<restricciones+1;i++){
            resultados[i-1]=(Float)procesado.getValueAt(i,procesado.getColumnCount()-2)/(Float)procesado.getValueAt(i, col);
            procesado.setValueAt(resultados[i-1],i,procesado.getColumnCount()-1);
        }   
        
        /*Pendiente lo validar si constante/entra es igual a 1 en la primera iteración que hacer, y que hacer en las siguientes 
       iteraciones si vuelve a aperecer un 1
        */
        
        Float cero=new Float(0);
        Float negativof=new Float(1000000000);
        int vsalida=0;
        for(int i=1;i<restricciones+1;i++){
            if((Float)procesado.getValueAt(i, procesado.getColumnCount()-1)>cero){
                if((Float)procesado.getValueAt(i, procesado.getColumnCount()-1)<negativof){
                    negativof=(Float)procesado.getValueAt(i, procesado.getColumnCount()-1);
                    vsalida=i;
                }
            }
        }

        System.out.print("\nPositivo menor de columna/entra= "+negativof+"\nFILA que sale= "+vsalida);  
        
        DefaultTableModel modelo = añadirEspacios(procesado);
        tablaSalida.setModel(modelo);
        //Llamando al método recursivo de iteraciones.
        iteracionesUno(modelo, vsalida, col);
    }
    
    
    public void procesoDos(DefaultTableModel procesado){
        //Realizando la primera iteración del método.
        Float positivo=new Float(0);
        //Preguntando por la COLUMNA que entra.
        int col=0;
        for(int i=1;i<procesado.getColumnCount()-3;i++){
            if(((Float)procesado.getValueAt(0, i))>new Float(0)){//se pregunta por el valor mas negativo de la matriz con los datos
                if(((Float)procesado.getValueAt(0, i))>positivo){
                    positivo=((Float)procesado.getValueAt(0, i));
                    col=i;
                }
            }
        }
        System.out.println("\nNumero mas positivo de Z= " +positivo+ "\nCOLUMNA que entra= " +col);
        
        //Preguntando por la FILA que sale.
        Float resultados[] = new Float[restricciones];
        for (int i=1;i<restricciones+1;i++){
            resultados[i-1]=(Float)procesado.getValueAt(i,procesado.getColumnCount()-2)/(Float)procesado.getValueAt(i, col);
            procesado.setValueAt(resultados[i-1],i,procesado.getColumnCount()-1);
        }   
        
        /*Pendiente lo validar si constante/entra es igual a 1 en la primera iteración que hacer, y que hacer en las siguientes 
       iteraciones si vuelve a aperecer un 1
        */
        
        Float cero=new Float(0);
        Float negativof=new Float(1000000000);
        int vsalida=0;
        for(int i=1;i<restricciones+1;i++){
            if((Float)procesado.getValueAt(i, procesado.getColumnCount()-1)>cero){
                if((Float)procesado.getValueAt(i, procesado.getColumnCount()-1)<negativof){
                    negativof=(Float)procesado.getValueAt(i, procesado.getColumnCount()-1);
                    vsalida=i;
                }
            }
        }

        System.out.print("\nPositivo menor de columna/entra= "+negativof+"\nFILA que sale= "+vsalida);  
        
        DefaultTableModel modelo = añadirEspacios(procesado);
        tablaSalida.setModel(modelo);
        //Llamando al método recursivo de iteraciones.
        iteracionesDos(modelo, vsalida, col);
    }
    
    public DefaultTableModel añadirEspacios(DefaultTableModel procesado){
      // ACA SE AGREGAN LOS ESPACIOS ENTRE TABLAS
      System.out.println("");
      contadoriteraciones=contadoriteraciones+1;
        
      espacio= new Object[procesado.getColumnCount()];        //new String[variables+holguras+4];
      Object [] lineaVacia= new Object[procesado.getColumnCount()];  //new String[variables+holguras+4];
      lineaVacia[0]=" ";
      espacio[0]=iteracion+" "+contadoriteraciones;
       
      for(int i=1;i<espacio.length;i++)
         {
             lineaVacia[i]=" ";
             espacio[i]=" ";
         }
       
      procesado.addRow(lineaVacia);
      procesado.addRow(espacio); 
      return procesado;
   } 
   
    
    public void iteracionesUno(DefaultTableModel procesado, int FilaqueSale, int ColqueEntra){
        //Realizando una copia de todo el modelo 'procesado'  en una matriz llamada 'arreglo' y 'arreglo2'.
        System.out.println();
        Object[][] arreglo= new Object[procesado.getRowCount()][procesado.getColumnCount()];
        for(int i=0;i<procesado.getRowCount();i++){
            for(int j=0;j<procesado.getColumnCount();j++){
                arreglo[i][j]=procesado.getValueAt(i, j);
                //System.out.print(arreglo[i][j]+ "     ");
            }
            //System.out.println();
        }
        
        //Creando la fila que entra. Volviendo 1 el elemento pivote y dividiendo la fila por ese valor. 
        Object fsalida []=new Object[procesado.getColumnCount()];
        fsalida[0]=procesado.getColumnName(ColqueEntra);
        //System.out.print("\nTamaño fsalida= "+fsalida.length);
        for(int i=1;i<fsalida.length;i++){
            fsalida[i]=(Float)arreglo[FilaqueSale][i]/(Float)arreglo[FilaqueSale][ColqueEntra];
        }
//        //Imprimiendo para prueba la fila que entra.
//        System.out.print("\nNueva fila que entra= ");
//        for(int i=0;i<fsalida.length;i++){
//            System.out.print(fsalida[i]+ "  ");
//        }
        
        //Realizando una copia de todo el modelo 'procesado'  en una matriz llamada 'arreglo' y 'arreglo2'.
        //Llenando arreglo2 con ceros.
        //System.out.println("\n");
        Object[][] arreglo2= new Object[procesado.getRowCount()][procesado.getColumnCount()];
        Object[][] arreglo3= new Object[procesado.getRowCount()][procesado.getColumnCount()];
        for(int i=0;i<procesado.getRowCount();i++){
            for(int j=0;j<procesado.getColumnCount();j++){
                if(i==FilaqueSale){
                    arreglo2[i][j]= fsalida[j];
                    arreglo3[i][j]=new Float(0);
                    //System.out.print(arreglo2[i][j]+ "     ");
                }
                else{
                    arreglo2[i][j]=procesado.getValueAt(i, j);
                    arreglo3[i][j]=new Float(0);
                    //System.out.print(arreglo2[i][j]+ "     ");
                }
            }
            //System.out.println();
        }
        
        
        int numFilas = procesado.getRowCount()-(restricciones+3);
        int numFilas2 = procesado.getRowCount()-2;
//        System.out.println("\nDesde FILA.... " + numFilas + "....Hasta FILA.... " + numFilas2) ;
//        System.out.println("Tamaño COLUMNAS..."+procesado.getColumnCount());
//        //Volviendo ceros los elementos arriba y abajo del elemento pivote.
        for(int fila=numFilas; fila<numFilas2; fila++){
            for(int columna=0; columna<procesado.getColumnCount(); columna++){
                if(columna==0){//Preguntado por la columna de nombres.
                   if(fila==FilaqueSale){
                     arreglo3[fila][columna] = fsalida[columna]; //Igualando a la nueva fila que entra.
                   }
                   else{
                       arreglo3[fila][columna] = arreglo2[fila][columna];
                   }
                }
                
                else {
                    if(columna != procesado.getColumnCount()-1){//No es la ultima columna. 
                       if(fila==FilaqueSale){//Fila que sale queda igual.
                          arreglo3[fila][columna] = fsalida[columna]; 
                       }
                       else{//Operación volver cero.
                           //System.out.println("\nF= "+fila+" C= "+columna);
                           arreglo3[fila][columna]= (Float)arreglo2[fila][columna] + ((Float)arreglo2[fila][ColqueEntra]*(new Float(-1))*
                                (Float)arreglo2[FilaqueSale][columna]);
                       }
                    }
                }
             }
            procesado.addRow(arreglo3[fila]);
       }
        
       //Preguntando nuevamente por la COLUMNA que entra, el valor más negativo de la iteración n.
        Float negativo=new Float(0);
        int col=0;
        for(int i=1;i<procesado.getColumnCount()-2;i++){
            if(((Float)procesado.getValueAt(procesado.getRowCount()-(restricciones+1), i))<new Float(0)){//se pregunta por el valor mas negativo de la matriz con los datos
                if(((Float)procesado.getValueAt(procesado.getRowCount()-(restricciones+1), i))<negativo){
                    negativo=((Float)procesado.getValueAt(procesado.getRowCount()-(restricciones+1), i));
                    col=i;
                }
            }
        }
        System.out.println("\nNumero mas negativo de Z= " +negativo+ "\nCOLUMNA que entra= " +col);
        
        //Preguntando si existe una columna que entra.
        if(col==0){
          //Asignando el modelo a la Tabla de Salida.
          //tablaSalida.setModel(procesado);
            
          //System.exit(0);
        }
        else{
            //Preguntando nuevamente por la FILA que sale.
            Float cero=new Float(0);
            Float negativof=new Float(100000);
            int vsalida=0, primerCero=0;
            boolean x=false;
             
            for (int i=procesado.getRowCount()-restricciones;i<procesado.getRowCount();i++){
                Float valor =(Float)procesado.getValueAt(i,procesado.getColumnCount()-2)/(Float)procesado.getValueAt(i, col);
                procesado.setValueAt(valor,i,procesado.getColumnCount()-1);
                if(valor>cero){
                    if(valor<negativof){
                        negativof=valor;
                        vsalida=i;
                    }
                }
                else if(valor==0){
                    if(x==false){
                        primerCero=i;
                        x=true;
                    }
                }
            }
        
            if(vsalida==0){ //Si todos los números de columna/entrada son cero se realizará.
               bandera++;  //una iteración más. Si persiste (bandera=2) y la iteración n es Óptima.
               //System.out.println("\nValor bandera = "+bandera);
               if(bandera == 2){
                  JOptionPane.showMessageDialog(null, "\nNo hay valores positivos para determinar la fila que "
                          + "sale. La iteración No." + contadoriteraciones + " es la SOLUCIÓN ÓPTIMA.");
               }
               else{
                 vsalida=primerCero; //La última fila SALE. 
                 //System.out.print("\n.......ENTRO.......");
                 System.out.print("\nPositivo menor de columna/entra= "+negativof+"\nFILA que sale= "+vsalida);
                 DefaultTableModel modelo = añadirEspacios(procesado); 
                 iteracionesUno(modelo, vsalida, col); 
               }
           }
           else{
               System.out.print("\nPositivo menor de columna/entra= "+negativof+"\nFILA que sale= "+vsalida);
               DefaultTableModel modelo = añadirEspacios(procesado); 
               iteracionesUno(modelo, vsalida, col); 
           }
      
        /*Pendiente lo validar si constante/entrante es igual a 1 en la primera iteración que hacer, y que hacer en las siguientes 
       iteraciones si vuelve a aperecer un 1
        */
      }
        
    }
    
    public void iteracionesDos(DefaultTableModel procesado, int FilaqueSale, int ColqueEntra){
        //Realizando una copia de todo el modelo 'procesado'  en una matriz llamada 'arreglo' y 'arreglo2'.
        System.out.println();
        Object[][] arreglo= new Object[procesado.getRowCount()][procesado.getColumnCount()];
        for(int i=0;i<procesado.getRowCount();i++){
            for(int j=0;j<procesado.getColumnCount();j++){
                arreglo[i][j]=procesado.getValueAt(i, j);
                //System.out.print(arreglo[i][j]+ "     ");
            }
            //System.out.println();
        }
        
        //Creando la fila que entra. Volviendo 1 el elemento pivote y dividiendo la fila por ese valor. 
        Object fsalida []=new Object[procesado.getColumnCount()];
        fsalida[0]=procesado.getColumnName(ColqueEntra);
        //System.out.print("\nTamaño fsalida= "+fsalida.length);
        for(int i=1;i<fsalida.length;i++){
            fsalida[i]=(Float)arreglo[FilaqueSale][i]/(Float)arreglo[FilaqueSale][ColqueEntra];
        }
//        //Imprimiendo para prueba la fila que entra.
//        System.out.print("\nNueva fila que entra= ");
//        for(int i=0;i<fsalida.length;i++){
//            System.out.print(fsalida[i]+ "  ");
//        }
        
        //Realizando una copia de todo el modelo 'procesado'  en una matriz llamada 'arreglo' y 'arreglo2'.
        //Llenando arreglo2 con ceros.
        //System.out.println("\n");
        Object[][] arreglo2= new Object[procesado.getRowCount()][procesado.getColumnCount()];
        Object[][] arreglo3= new Object[procesado.getRowCount()][procesado.getColumnCount()];
        for(int i=0;i<procesado.getRowCount();i++){
            for(int j=0;j<procesado.getColumnCount();j++){
                if(i==FilaqueSale){
                    arreglo2[i][j]= fsalida[j];
                    arreglo3[i][j]=new Float(0);
                    //System.out.print(arreglo2[i][j]+ "     ");
                }
                else{
                    arreglo2[i][j]=procesado.getValueAt(i, j);
                    arreglo3[i][j]=new Float(0);
                    //System.out.print(arreglo2[i][j]+ "     ");
                }
            }
           // System.out.println();
        }
        
        
        int numFilas = procesado.getRowCount()-(restricciones+3);
        int numFilas2 = procesado.getRowCount()-2;
//        System.out.println("\nDesde FILA.... " + numFilas + "....Hasta FILA.... " + numFilas2) ;
//        System.out.println("Tamaño COLUMNAS..."+procesado.getColumnCount());
//        //Volviendo ceros los elementos arriba y abajo del elemento pivote.
        for(int fila=numFilas; fila<numFilas2; fila++){
            for(int columna=0; columna<procesado.getColumnCount(); columna++){
                if(columna==0){//Preguntado por la columna de nombres.
                   if(fila==FilaqueSale){
                     arreglo3[fila][columna] = fsalida[columna]; //Igualando a la nueva fila que entra.
                   }
                   else{
                       arreglo3[fila][columna] = arreglo2[fila][columna];
                   }
                }
                
                else {
                    if(columna != procesado.getColumnCount()-1){//No es la ultima columna. 
                       if(fila==FilaqueSale){//Fila que sale queda igual.
                          arreglo3[fila][columna] = fsalida[columna]; 
                       }
                       else{//Operación volver cero.
                           //System.out.println("\nF= "+fila+" C= "+columna);
                           arreglo3[fila][columna]= (Float)arreglo2[fila][columna] + ((Float)arreglo2[fila][ColqueEntra]*(new Float(-1))*
                                (Float)arreglo2[FilaqueSale][columna]);
                       }
                    }
                }
             }
            procesado.addRow(arreglo3[fila]);
       }
        
       //Preguntando nuevamente por la COLUMNA que entra, el valor más positivo de la iteración n.
        Float positivo=new Float(0);
        int col=0;
        for(int i=1;i<procesado.getColumnCount()-2;i++){
            if(((Float)procesado.getValueAt(procesado.getRowCount()-(restricciones+1), i))>new Float(0)){
                if(((Float)procesado.getValueAt(procesado.getRowCount()-(restricciones+1), i))>positivo){
                    positivo=((Float)procesado.getValueAt(procesado.getRowCount()-(restricciones+1), i));
                    col=i;
                }
            }
        }
        System.out.println("\nNumero mas positivo de Z= " +positivo+ "\nCOLUMNA que entra= " +col);
        
        //Preguntando si existe una columna que entra.
        if(col==0){
          //Asignando el modelo a la Tabla de Salida.
          //tablaSalida.setModel(procesado);
          //JOptionPane.showMessageDialog(null, "La iteración No." + contadoriteraciones + " es la SOLUCIÓN ÓPTIMA.");  
          //System.exit(0);
        }
        else{
            //Preguntando nuevamente por la FILA que sale.
            Float cero=new Float(0);
            Float negativof=new Float(1000000000);
            int vsalida=0, primerCero=0;
            boolean x=false;
             
            for (int i=procesado.getRowCount()-restricciones;i<procesado.getRowCount();i++){
                Float valor =(Float)procesado.getValueAt(i,procesado.getColumnCount()-2)/(Float)procesado.getValueAt(i, col);
                procesado.setValueAt(valor,i,procesado.getColumnCount()-1);
                if(valor>cero){
                    if(valor<negativof){
                        negativof=valor;
                        vsalida=i;
                    }
                }
                else if(valor==0){
                    if(x==false){
                        primerCero=i;
                        x=true;
                    }
                }
            }
        
            
            if(vsalida==0){ //Si todos los números de columna/entrada son cero se realizará.
               bandera++;  //una iteración más. Si persiste (bandera=2) y la iteración n es Óptima.
               //System.out.println("\nValor bandera = "+bandera);
               if(bandera == 2){
                  JOptionPane.showMessageDialog(null, "\nNo hay valores positivos para determinar la fila que "
                          + "sale. La iteración No." + contadoriteraciones + " es la SOLUCIÓN ÓPTIMA.");
               }
               else{
                 vsalida=primerCero; //La última fila SALE.
                 //System.out.print("\n.......ENTRO.......");
                 System.out.print("\nPositivo menor de columna/entra= "+negativof+"\nFILA que sale= "+vsalida);
                 DefaultTableModel modelo = añadirEspacios(procesado); 
                 iteracionesDos(modelo, vsalida, col); 
               }
           }
           else{
               System.out.print("\nPositivo menor de columna/entra= "+negativof+"\nFILA que sale= "+vsalida);
               DefaultTableModel modelo = añadirEspacios(procesado); 
               iteracionesDos(modelo, vsalida, col); 
           }
      
        /*Pendiente lo validar si constante/entrante es igual a 1 en la primera iteración que hacer, y que hacer en las siguientes 
       iteraciones si vuelve a aperecer un 1
        */
      }
        
    }
    
    
    
       public TableModel getModelTabla()
    {
        return tablaSalida.getModel();
    }
    
    
}