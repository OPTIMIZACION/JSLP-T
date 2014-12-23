/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package optimizacion;

import java.io.File;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author MAICK
 */
public class Simplex 
{
    String mSalida[][];
    int variables, restricciones;
    DefaultTableModel md;
    JTable jTable1;
    JTable tabla;
    JTable tablaprincipal;
    String []nombrecolumnas;
    String []nombrecolumnas2;
    //String [][]campos;
    String [][]procesado;
    String []espacio;
    String []fsalida;
    String [][]newdatos;
    String [][]datos;/////////////////
    Double []resultados;
    String [][]tablaprincipal1;
    Double negativo; 
    int contadoriteraciones=0;
    String iteracion="Iteracion";
    Double colum;
    
    public Simplex(TableModel md, int variables, int restricciones)
    {
        System.out.println("entro a constructor simplex");
        
        jTable1=new JTable(md);
        this.variables=variables;
        this.restricciones=restricciones;
        
        captura();
        proceso();
    }
    
    
    //----------------------------------------------------------
    //------METODO EN QUE SE GUARDA LA TABLA PRINCIPAL----------
    //----------------------------------------------------------
    public void principal(String []nombre){
        
        System.out.println("entro a metodo principal");
        
        tablaprincipal1=new String[jTable1.getRowCount()][jTable1.getColumnCount()];
        for(int i=0;i<jTable1.getRowCount();i++){
            for(int j=0;j<jTable1.getColumnCount();j++){
                tablaprincipal1[i][j]=jTable1.getValueAt(i, j).toString();
            }
        }
        md= new DefaultTableModel(tablaprincipal1, nombre);
        tablaprincipal= new JTable(md);
        jTable1.setModel(md);
    }
    
    //----------------------------------------------------------
    //-----------METODO EN QUE SE CAPTURAN LOS DATOS------------
    //----------------------------------------------------------
    public String[][] captura(){
        
        System.out.println("entro a metodo captura");
        
        //{1}se capturan los datos de la tabla
        datos=new String[jTable1.getRowCount()-3][jTable1.getColumnCount()];
        for(int i=1;i<=jTable1.getRowCount()-3;i++){
            for(int j=0;j<jTable1.getColumnCount();j++){
                datos[i-1][j]=jTable1.getValueAt(i, j).toString();
            }
        }
        
        return datos;
    }
    
    
    //----------------------------------------------------------
    //-METODO EN QUE SE LLENA LA TABLA CON LOS DATOS INICIALES--
    //----------------------------------------------------------
    // SE TOMAN LOS DATOS CAPTURADOS Y EMPEZARA LA PRIMERA ITERACION
    public void proceso(){
        
        System.out.println("entro a metodo proceso");
        
        int cont=0;
        System.out.println("largo datos "+datos.length);
        
        for(int i=1;i<restricciones+1;i++){
            if(datos[i][variables+1].equals("<=")){
                cont=cont+1;
            }
            if(datos[i][variables+1].equals(">=")){
                cont=cont+1;
            }
        }
        System.out.println("numero de <= "+cont);
        
        procesado=new String[restricciones+1][variables+4+cont];
        for(int i=0;i<restricciones+1;i++){
            procesado[i][0]=datos[i][0];
        }
        for(int i=0;i<restricciones+1;i++){
            if(i==0){
                procesado[i][1]="1";
            }else{
                procesado[i][1]="0";
            }
        }
        
        for(int i=1;i<variables+1;i++){
            for(int j=0;j<restricciones+1;j++){
                procesado[j][i+1]=datos[j][i];
            }
        }
        
        for(int i=variables+2;i<variables+cont+2;i++){
            for(int j=0;j<restricciones+1;j++){
                procesado[j][i]="0";
            }
        }
        
        for(int j=1;j<restricciones+1;j++){
            if(datos[j][variables+1].equals("<=")){
                procesado[j][variables+j+1]="1";
            }
            if(datos[j][variables+1].equals(">=")){
                procesado[j][variables+j+1]="-1";
            }
        }
        
        
        for(int i=0;i<restricciones+1;i++){
            procesado[i][variables+cont+2]=datos[i][variables+2];
            //procesado[i][variables+cont+2]="0";
        }
        
        //SE LE ASIGNAN LOS NOMBRES A LA FILA DE NOMBRES
        nombrecolumnas2=new String[variables+cont+4];
        nombrecolumnas2[1]="Z";
        for(int i=2;i<variables+2;i++){
            nombrecolumnas2[i]="X"+(i-1);
        }
        int c=1;
        for(int i=variables+1;i<variables+cont+1;i++){
            nombrecolumnas2[i+1]="H"+c;
            c=c+1;
        }
        nombrecolumnas2[variables+cont+2]="constante";
        nombrecolumnas2[variables+cont+3]="constante/entrada";
        
        //se envian los datos de las columnas al metodo que guardara la tabla principal
        principal(nombrecolumnas);
        
        //AQUI COMIENZA LA PRIMERA ITERACION
        negativo=new Double(0);
        int col=0;
        for(int i=2;i<variables+2;i++){
            if((Double.parseDouble(procesado[0][i])*(-1))<0){//se pregunta por el valor mas negativo de la matriz con los datos
                if((Double.parseDouble(procesado[0][i])*(-1))<negativo){
                    negativo=(Double.parseDouble(procesado[0][i])*(-1));
                    col=i;
                }
            }
        }
        System.out.println("Numero mas negativo "+negativo+" en la columna "+col);

        resultados= new Double[restricciones];
        for (int i=1;i<restricciones+1;i++){
            resultados[i-1]=Double.parseDouble(procesado[i][variables+cont+2])/Double.parseDouble(procesado[i][col]);
            procesado[i][variables+cont+3]=String.valueOf(resultados[i-1]);
        }
        
        for(int i=0;i<restricciones+1;i++){
            for(int j=0;j<variables+3+cont;j++){
                System.out.print(procesado[i][j].toString()+"   ");
            }
            System.out.println();
        }
        
        Double cero=new Double(0);
        Double negativof=new Double(100000);
        int vsalida=0;
        for(int i=1;i<restricciones+1;i++){
            if(Double.parseDouble(procesado[i][variables+cont+3])>=cero){
                if(Double.parseDouble(procesado[i][variables+cont+3])<negativof){
                    negativof=Double.parseDouble(procesado[i][variables+cont+2]);
                    vsalida=i;
                }
            }
        }
        int holguras=cont;
        //mostrartabla(procesado, nombrecolumnas2);
        md= new DefaultTableModel(procesado, nombrecolumnas2);
        tabla= new JTable(md);
        //panelTrabajo.setViewportView(tabla);
        
        iteraciones(vsalida,col,holguras,procesado);
    }
    
    //----------------------------------------------------------
    //-------METODO EN QUE SE REALIZAN LAS ITERACIONES----------
    //----------------------------------------------------------
    public void iteraciones(int vsalida,int col,int holguras,String [][]procesado){
        
        System.out.println("entro a metodo iteraciones");
        
        try{
        System.out.println();
        contadoriteraciones=contadoriteraciones+1;
        espacio=new String[variables+holguras+4];
        String [] lineaVacia=new String[variables+holguras+4];
        
        lineaVacia[0]=" ";
        espacio[0]=iteracion+" "+contadoriteraciones;
        for(int i=1;i<espacio.length;i++)
        {
            lineaVacia[i]=" ";
            espacio[i]=" ";
        }
        
        md.addRow(lineaVacia);
        md.addRow(espacio); // ACA SE AGREGAN LOS ESPACIOS ENTRE TABLAS
        fsalida=new String[variables+holguras+4];
        newdatos=new String[restricciones+1][variables+holguras+2];
        
        for(int i=1;i<variables+holguras+4;i++){
            fsalida[i]=String.valueOf(Double.parseDouble(procesado[vsalida][i])/Double.parseDouble(procesado[vsalida][col]));
        }
        
        fsalida[0]=nombrecolumnas2[col];
        
        for(int i=0;i<variables+holguras+4;i++){
            System.out.print(fsalida[i]+ "  ");
        }
        
        for(int i=0;i<restricciones+1;i++){
            colum=Double.parseDouble(procesado[i][col]);
            for(int j=2;j<variables+holguras+3;j++){
                if(i==0){
                    espacio[1]="1";
                }else{
                    espacio[1]="0";
                }
                if(i==vsalida){
                    espacio[0]=fsalida[0];
                    procesado[i][0]=fsalida[0];
                    espacio[j]=fsalida[j];
                }else{
                    espacio[j]=String.valueOf((Double.parseDouble(fsalida[j])*(colum*(new Double(-1))))+Double.parseDouble(procesado[i][j]));
                }
                
                procesado[i][j]=espacio[j];
            }
            espacio[variables+holguras+3]=String.valueOf(Double.parseDouble(procesado[i][variables+holguras+2])/Double.parseDouble(procesado[i][col]));
        md.addRow(procesado[i]);    
        }
        
        negativo=new Double(0);
        col=0;
        for(int i=2;i<variables+2;i++){
            if(Double.parseDouble(procesado[0][i])<0){
                if(Double.parseDouble(procesado[0][i])<negativo){
                    negativo=Double.parseDouble(procesado[0][i]);
                    col=i;
                }
            }
        }
        System.out.println();
        System.out.println();
        for(int i=0;i<restricciones+1;i++){
            for(int j=0;j<variables+3+holguras;j++){
                System.out.print(procesado[i][j].toString()+"   ");
            }
            System.out.println();
        }
        
        resultados= new Double[restricciones];
        for (int i=1;i<restricciones+1;i++){
            resultados[i-1]=Double.parseDouble(procesado[i][variables+holguras+2])/Double.parseDouble(procesado[i][col]);
            procesado[i][variables+holguras+3]=String.valueOf(resultados[i-1]);
        }
        
        Double cero=new Double(0);
        Double negativof=new Double(100000);
        vsalida=0;
        for(int i=1;i<restricciones+1;i++){
            if(Double.parseDouble(procesado[i][variables+holguras+3])>cero){
                if(Double.parseDouble(procesado[i][variables+holguras+3])<negativof){
                    negativof=Double.parseDouble(procesado[i][variables+holguras+3]);
                    vsalida=i;
                }
            }
        }
        
        for(int i=1;i<variables+1;i++){
            if(Double.parseDouble(procesado[0][i])<0){
                iteraciones(vsalida,col,holguras,procesado);
            }
        }
        }catch(Exception e){
          System.out.print(e);
        }
    }
    
    
    
    
    public TableModel getModelTabla()
    {
        return tabla.getModel();
    }
    
    public TableModel getModeljTable1()
    {
        return jTable1.getModel();
    }
    
    public TableModel getModelTablaPrincipal()
    {
        return tablaprincipal.getModel();
    }
    
    public String[][] getProcesado()
    {
        return procesado;
    }
    public String[] getnombrecolumnas2()
    {
        return nombrecolumnas2;
    }
    
}
