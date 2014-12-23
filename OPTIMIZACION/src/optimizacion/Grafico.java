

package optimizacion;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import java.lang.Math;
import javax.swing.JTable;
import javax.swing.table.TableModel;


/**
 *
 * @author Tali
 */
public class Grafico extends javax.swing.JFrame {
    int var,res;
    JTextField [][]c;
    DefaultTableModel model;
    Double x1,x2;
    Double [][][]puntos;
    Double [][]puntos2;
    String data[][]={};
    String cabeza[]={"Coordenada X(x1)","Coordenada Y(x1)","Valor de la funcion(Z)"};
    Double  m;
    Menu menu;
    int cont=0;
    double valorf1=0;
    String dato;
    int variables;
    int restricciones;
    JTable jTable1;

    public Grafico(TableModel md, int variables, int restricciones) {
        initComponents();
        jTable1=new JTable(md);
        this.variables=variables;
        this.restricciones=restricciones;
        paneltabla.setVisible(false);
        glineal.setVisible(false);
        capturar();
    }
    
    public void capturar(){
        this.var=variables+1;
        this.res=restricciones;
        Double [][]matriz=new Double[this.var][this.res];
        for(int i=1;i<3;i++){
            for(int j=2;j<this.res+2;j++){
                matriz[i-1][j-2]=Double.parseDouble(jTable1.getValueAt(j,i).toString());
            }
        }
        for(int i=2;i<res+2;i++){
            matriz[variables][i-2]=Double.parseDouble(jTable1.getValueAt(i,4).toString());
        }
        /*Double [][]matriz=new Double[this.var][this.res];
        for(int i=0;i<this.var;i++){
            for(int j=0;j<this.res;j++){
                matriz[i][j]=Double.parseDouble(c[i][j].getText());    
            }
        }
        for(int i=0;i<this.res;i++){
            for(int j=0;j<this.var;j++){
                System.out.print(matriz[j][i]+" ");
            }
            System.out.println();
        }*/
        despejar(matriz);
    }
    
    public void despejar(Double [][]matriz){
        puntos=new Double[this.res][2][3];
        
        int h=0;
        for(int i=0;i<this.res;i++){
            for(int j=0;j<2;j++){
                if(j==0){
                    x1=new Double(0);
                    x2=matriz[2][i]/matriz[1][i];
                    puntos[i][0][0]=x1;
                    puntos[i][0][1]=x2;
                    puntos[i][0][2]=(x1*Double.parseDouble(jTable1.getValueAt(1,1).toString()))+(x2*Double.parseDouble(jTable1.getValueAt(1,2).toString()));
                    
                }else{
                    x1=matriz[2][i]/matriz[0][i];
                    x2=new Double(0);
                    puntos[i][1][0]=x1;
                    puntos[i][1][1]=x2;
                    puntos[i][1][2]=(x1*Double.parseDouble(jTable1.getValueAt(1,1).toString()))+(x2*Double.parseDouble(jTable1.getValueAt(1,2).toString()));
                }  
                h++;
            }      
        }
        
               
        
        
        
        /*for(int i=0;i<this.res;i++){
            for(int j=0;j<3;j++){
                for(int k=0;k<2;k++){
                    System.out.print(puntos[i][k][j]+" ");
                }
                System.out.println();
            }
            System.out.println();
        }*/
        diagrama(puntos);    
    }
    
    public void diagrama(Double puntos[][][]){
    XYSeries []series=new XYSeries[this.res];
    for(int i=0;i<this.res;i++){
        series[i]=new XYSeries("Datos "+(i+1));
        series[i].add(puntos[i][0][0],puntos[i][0][1]);
        series[i].add(puntos[i][1][0],puntos[i][1][1]);
    }
    glineal.removeAll();
    //System.out.println(puntos.length);
    
    XYSeriesCollection dataset =new XYSeriesCollection();
    for(int i=0;i<this.res;i++){
        dataset.addSeries(series[i]);
    }
    
                        
                        
//  JFreeChart chart =ChartFactory.createXYLineChart(" Grafico xy","Eje x", "Eje y", dataset, PlotOrientation.VERTICAL, true, true, false);
    JFreeChart chart =ChartFactory.createXYAreaChart(" Grafico Lineal","Eje x", "Eje y", dataset, PlotOrientation.VERTICAL, true, true, false);

          //    ChartFrame frame =new ChartFrame("Metodo Grafico",chart); 
  //  frame.pack(); 
   // frame.setVisible(true);
    //frame.setLocationRelativeTo(null);
                        
                        ChartPanel panel;
                       panel =new ChartPanel(chart);
                       
                       panel.setBounds(0,0,glineal.getSize().width,glineal.getSize().height);
                       glineal.add(panel);
                       glineal.repaint();  
                       glineal.setVisible(true);
                       
    tabla();
}
    
    public void tabla(){
        paneltabla.setVisible(true);
        puntos2=new Double[this.res*2][3];
        for(int i=0;i<this.res;i++){
            for(int j=0;j<2;j++){
                if(j==0){
                    puntos2[i][0]=puntos[i][0][0];
                    puntos2[i][1]=puntos[i][0][1];
                    puntos2[i][2]=puntos[i][0][2];
                }else{
                    puntos2[i+this.res][0]=puntos[i][1][0];
                    puntos2[i+this.res][1]=puntos[i][1][1];
                    puntos2[i+this.res][2]=puntos[i][1][2];
                }
            }
        }
        
        model=new DefaultTableModel(puntos2,cabeza);
        tablad.setModel(model);
        
       f.setText(jTable1.getValueAt(1,1).toString()+" X1+ " +jTable1.getValueAt(1,2).toString()+ " X2");
       
       /*int objetivo=objfun.getSelectedIndex();
        if (objetivo == 0) {*/
        double max=0;
        for(int i=0;i<this.res;i++){
           for(int j=0;j<2;j++){
                m=puntos[i][j][2];
                if(m>max){ 
                    max=m;
                }
            }
        }
        System.out.println(" max = "+max);
        //o.setText("  "+max); 
        
        /*}else if (objetivo == 1) {
        double min=90000;
        for(int i=0;i<this.res;i++){
            for(int j=0;j<2;j++){
                m=puntos[i][j][2];
                if(m<min){ 
                    min=m;
                }
            }   
        }
        System.out.println(" min = "+min);
        o.setText("  "+min); 
        }*/

       
        
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        glineal = new javax.swing.JPanel();
        paneltabla = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablad = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        f = new javax.swing.JLabel();

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        glineal.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout glinealLayout = new javax.swing.GroupLayout(glineal);
        glineal.setLayout(glinealLayout);
        glinealLayout.setHorizontalGroup(
            glinealLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        glinealLayout.setVerticalGroup(
            glinealLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 427, Short.MAX_VALUE)
        );

        paneltabla.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 0, 51)));

        tablad.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tablad);

        jLabel7.setText("Funcion:");

        f.setText("jLabel8");

        javax.swing.GroupLayout paneltablaLayout = new javax.swing.GroupLayout(paneltabla);
        paneltabla.setLayout(paneltablaLayout);
        paneltablaLayout.setHorizontalGroup(
            paneltablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, paneltablaLayout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(f)
                .addGap(150, 150, 150)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        paneltablaLayout.setVerticalGroup(
            paneltablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(paneltablaLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(paneltablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(f))
                .addContainerGap(50, Short.MAX_VALUE))
            .addGroup(paneltablaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(paneltabla, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(glineal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(glineal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(paneltabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel f;
    private javax.swing.JPanel glineal;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JPanel paneltabla;
    private javax.swing.JTable tablad;
    // End of variables declaration//GEN-END:variables
}
