/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package optimizacion;


import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author MAICK-PC
 */
public class VentanaEntradaDatos1 extends javax.swing.JFrame {

    String [] columnas;
    String [][] filas;
    //DefaultTableModel modelo;
    String mSalida[][];
    ArrayList <String> salida;
    String ruta;
    File archivo;
    JTable jTable1;
    int origenes, destinos;
    boolean bandera=true;
    
    
    public VentanaEntradaDatos1() {
        
        initComponents();
        this.setLocationRelativeTo(null);
        
        campoTitulo.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                campoTitulo.selectAll();
            }
        });
        
        
    }
    
    public VentanaEntradaDatos1(String titulo, int origenes, int destinos, String[][] matrizG) 
    {
        initComponents();
        this.setLocationRelativeTo(null);
        
        campoTitulo.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                campoTitulo.selectAll();
            }
        });
        campoTitulo.setText(titulo);
        campoVariables.setText(Integer.toString(origenes));
        campoRestricciones.setText(Integer.toString(origenes));
        this.origenes=origenes;
        this.destinos=destinos;
                

        //llenando titulos de la tabla
        this.columnas=new String[destinos+3];
        this.columnas[0]=" ";
        this.columnas[1]=" ";
        int i=0;
        for (i=2 ; i <= destinos+1; i++) 
        {
            this.columnas[i]="D"+i;
        }
        this.columnas[i++]="Oferta";

        
        
        
        this.filas=matrizG;
        
        desplegarTabla(filas, columnas);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        campoVariables = new javax.swing.JTextField();
        campoRestricciones = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        campoTitulo = new javax.swing.JTextField();
        BotonResolver = new javax.swing.JButton();
        botonMenu = new javax.swing.JButton();
        panelTrabajo = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTextArea1.setEditable(false);
        jTextArea1.setBackground(new java.awt.Color(220, 220, 220));
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("Instrucciones:\n>Ingrese un titulo para el problema.\n>Ingrese la canidad de variables.\n>Ingrese la cantidad de restricciones.\n>Click en \"Generar\" para crear la tabla donde ingresara los datos.\n>Una vez halla completado los datos necesarios oprima el boton \"Resolver Problema\".\n> Se le pedira si desea guardar los datos del problema para futuros cambios.");
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 651, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        campoVariables.setText("2");

        campoRestricciones.setText("3");

        jLabel1.setText("Nro Origen:");

        jLabel2.setText("Nro Destino:");

        jLabel3.setText("Nombre");

        jButton1.setText("Generar");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonGenerar(evt);
            }
        });

        campoTitulo.setText(" ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(campoTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addGap(41, 41, 41)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(campoVariables, javax.swing.GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                .addComponent(campoRestricciones)))))
                .addContainerGap(74, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(campoTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(campoVariables, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(campoRestricciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        BotonResolver.setText("Resolver Problema");
        BotonResolver.setEnabled(false);
        BotonResolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonresolver(evt);
            }
        });

        botonMenu.setText("MENU");
        botonMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                botonMenuMousePressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(394, 394, 394)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(botonMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BotonResolver, javax.swing.GroupLayout.DEFAULT_SIZE, 132, Short.MAX_VALUE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(panelTrabajo)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTrabajo, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(BotonResolver)
                .addGap(1, 1, 1)
                .addComponent(botonMenu)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
 
    private void botonGenerar(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonGenerar
       
        String titulo=campoTitulo.getText();
        
        int error=1;
        
        try
        {
            this.origenes= Integer.parseInt(campoVariables.getText());
            this.destinos= Integer.parseInt(campoRestricciones.getText());
            
            columnas= new String[destinos+3];
            filas=new String[origenes+2][destinos+3];


            //llenando titulos de la tabla
            this.columnas=new String[destinos+3];
            this.columnas[0]=" ";
            this.columnas[1]=" ";
            int i=0;
            for (i=2 ; i <= destinos+1; i++) 
            {
                this.columnas[i]="D"+i;
            }
            this.columnas[i++]="Oferta";




            //inicializando toda la matriz con valores vacios
            for (int fila = 0; fila < filas.length; fila++) 
            {
                for (int columna = 0; columna < filas[fila].length; columna++) 
                {
                    filas[fila][columna]=" "; 
                }
            }


            //colocando 0.0 en los campos correspondientes de las variables
            for (int fila = 1; fila < filas.length-1; fila++) 
            {
                for (int columna = 2; columna < filas[fila].length-1; columna++) 
                {
                    filas[fila][columna]="0.0";
                }
            }


            
            
            filas[0][1]="Nombres"; 


            //llenando matriz de filas 1er columna
            filas[0][0]=" ";
            int j=0;
            for (j=1; j < origenes+1; j++) {
               filas[j][0]="O"+(j-1); 
            }
            filas[j++][0]="Demanda";


            desplegarTabla(filas,columnas);
        
        }catch(Exception e)
        {
            JOptionPane.showMessageDialog(this, "¡los valores deben ser numeros!");
        }
        
        
        
        
        
        
                
    }//GEN-LAST:event_botonGenerar

    private void desplegarTabla(String[][] filas, String[] columnas)
    {
        
        jTable1=new JTable(filas, columnas)
        {
            
            @Override
            public boolean isCellEditable(int fila, int columna)
            {
                boolean respuesta=true;
                //Aqui deshabilito toda la primera columna de ser editable
                if (columna == 0  ) 
                {
                    return false;
                }
                else if(fila==0 && columna==1)
                {
                    return false;
                }
                else if(fila==origenes+1 && columna==1)
                {
                    return false;
                }
                else if(fila==0 && columna==destinos+2)
                {
                    return false;
                }
                else if(fila==origenes+1 && columna==destinos+2)
                {
                    return false;
                }
                else 
                {
                    return true;
                }
                              
            }
            
            
            public Component prepareRenderer(TableCellRenderer renderer, int fila, int columna) {
                // get the current row
                Component comp = super.prepareRenderer(renderer, fila, columna);
                // even index, not selected
                
                if (columna == 0 && !isCellSelected(fila, columna)  ) 
                {
                    comp.setBackground(Color.LIGHT_GRAY);
                }
                else if(fila==0 && columna==1)
                {
                    comp.setBackground(Color.LIGHT_GRAY);
                }
                else if(fila==origenes+1 && columna==1)
                {
                    comp.setBackground(Color.LIGHT_GRAY);
                }
                else if(fila==0 && columna==destinos+2)
                {
                    comp.setBackground(Color.LIGHT_GRAY);
                }
                else if(fila==origenes+1 && columna==destinos+2)
                {
                    comp.setBackground(Color.LIGHT_GRAY);
                }
                else 
                {
                    comp.setBackground(Color.white);
                }
                return comp;
            }
        
        };

        
        jTable1.addKeyListener(
            new KeyListener(){
                
                @Override
                public void keyPressed(KeyEvent e)
                {
                    int fila=jTable1.getSelectedRow();
                    int columna=jTable1.getSelectedColumn();
                    
                    int keyCode = e.getKeyCode();
                    System.out.println(fila+"f "+columna+"c");
                    System.out.println(e.getKeyChar()+" : "+e.getKeyCode());
                    
                    if(jTable1.isCellEditable(fila, columna) && fila!=0 && columna!=1 )
                    {
                        char caracter = e.getKeyChar();
                        
                        if(KeyEvent.VK_DOWN==keyCode||keyCode==KeyEvent.VK_UP||keyCode==KeyEvent.VK_LEFT||keyCode==KeyEvent.VK_RIGHT){
                           bandera=true; 
                        }
                        else if(((caracter < '0') || (caracter > '9')) && (caracter != KeyEvent.VK_BACK_SPACE) && (caracter != 46))
                        {
                            e.consume();
                        }
                        else
                        {
                            if(bandera)
                            {
                                bandera=false;
                                jTable1.setValueAt("", fila, columna);
                            }
                        }

                    }
                    else if(jTable1.isCellEditable(fila, columna) && (columna==1 || fila==0))
                    {
                        char caracter = e.getKeyChar();
                        
                        if(KeyEvent.VK_DOWN==keyCode||keyCode==KeyEvent.VK_UP||keyCode==KeyEvent.VK_LEFT||keyCode==KeyEvent.VK_RIGHT){
                           bandera=true; 
                        }
                        
                        else
                        {
                            if(bandera)
                            {
                                bandera=false;
                                jTable1.setValueAt("", fila, columna);
                            }
                        }
                        
                    }
                    
                }

                @Override
                public void keyTyped(KeyEvent ke) {
                }

                @Override
                public void keyReleased(KeyEvent ke) {
                }
            }
        );
        
        
        this.jTable1.setAutoResizeMode(0);
        this.jTable1.setCellSelectionEnabled(false);
        //agrega la tabla al jScrollPane
        panelTrabajo.setViewportView(this.jTable1);
        
        //habilita el boton para poder guardar y resolver
        BotonResolver.setEnabled(true);
         
    }
    
     private void save(){
                                
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("todos los archivos *.TXT", "txt","TXT"));//filtro para ver solo archivos .txt
            int seleccion = fileChooser.showSaveDialog(null);
            try{
                if (seleccion == JFileChooser.APPROVE_OPTION){//comprueba si ha presionado el boton de aceptar
                    File JFC = fileChooser.getSelectedFile();
                    String PATH = JFC.getAbsolutePath();//obtenemos el path del archivo a guardar
                    try (PrintWriter printwriter = new PrintWriter(JFC)) {
                        
                        for(String Object:salida)
                        {
                            if(Object!=null)
                            {
                                printwriter.println(Object);
                            }
                            else
                            {
                                printwriter.println(" ");
                            }
                        }
                        
                        printwriter.close();
                        
                    } //escribe en el archivo todo lo que se encuentre en el JTextArea
                    catch(Exception e)
                    {
                        JOptionPane.showMessageDialog(this,"Error en la escritura");
                    }
                    //comprobamos si a la hora de guardar obtuvo la extension y si no se la asignamos
                    if(!(PATH.endsWith(".txt"))){
                        File temp = new File(PATH+".txt");
                        JFC.renameTo(temp);//renombramos el archivo
                    }
                    
                    JOptionPane.showMessageDialog(null,"Guardado exitoso!", "Guardado exitoso!", JOptionPane.INFORMATION_MESSAGE);
                }
            }catch (Exception e){//por alguna excepcion salta un mensaje de error
                JOptionPane.showMessageDialog(null,"Error al guardar el archivo!", "Oops! Error", JOptionPane.ERROR_MESSAGE);
            }
        //}           
    }
    
    private void botonresolver(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonresolver
 salida=new ArrayList<String>();
        
        salida.add("5000");
        salida.add("5");
        salida.add("2");
        salida.add("1");
        salida.add(campoTitulo.getText());        
        salida.add(""+origenes);
        salida.add(""+destinos);
        
        
        mSalida= new String[jTable1.getRowCount()][jTable1.getColumnCount()];
        
        for(int i=0; i< jTable1.getRowCount(); i++) //recorro las filas
        {
         for(int a=0; a< jTable1.getColumnCount(); a++) //recorro las columnas
         {
          
          mSalida[i][a]= jTable1.getValueAt(i, a).toString();
          salida.add(mSalida[i][a]);
          
         }
        }
        
        for (String string : salida) {
            System.out.println(string);
        }
        
        
        
        //PREGUNTAR SI DESEA GUARDAR
        int seleccion = JOptionPane.showOptionDialog(this,"¿Desea guardar los datos?", "Guardar Archivo",JOptionPane.YES_NO_CANCEL_OPTION,
                                                    JOptionPane.QUESTION_MESSAGE,
                                                    null,    // null para icono por defecto.
                                                    new Object[] { "Si", "No", "Cancelar" },   // null para YES, NO y CANCEL
                                                    "opcion 1");
        if(seleccion==0)//si desea guardar
        {
            save();
            Salida1 resolver=new Salida1(jTable1.getModel(), campoTitulo.getText(),campoVariables.getText(),campoRestricciones.getText() );
            resolver.setVisible(true);
            this.setVisible(false);
            
        }
        else if(seleccion==1)//no desea guardar
        {
            Salida1 resolver=new Salida1(jTable1.getModel(), campoTitulo.getText(),campoVariables.getText(),campoRestricciones.getText() );
            resolver.setVisible(true);
            this.setVisible(false);
        }
    }//GEN-LAST:event_botonresolver

    private void botonMenuMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_botonMenuMousePressed
        Menu menu=new Menu();
        menu.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_botonMenuMousePressed



    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VentanaEntradaDatos1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VentanaEntradaDatos1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VentanaEntradaDatos1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VentanaEntradaDatos1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VentanaEntradaDatos1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BotonResolver;
    private javax.swing.JButton botonMenu;
    private javax.swing.JTextField campoRestricciones;
    private javax.swing.JTextField campoTitulo;
    private javax.swing.JTextField campoVariables;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JScrollPane panelTrabajo;
    // End of variables declaration//GEN-END:variables
}
