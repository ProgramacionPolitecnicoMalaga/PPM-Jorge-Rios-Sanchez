package com.politecnicomalaga.vista;

import com.politecnicomalaga.controlador.ControladorMensajes;
import com.politecnicomalaga.controlador.ControladorUsuarios;
import com.politecnicomalaga.modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;
import java.util.ArrayList;

public class PanelEscrituraMensajes {
    private JPanel pnlEscritura;
    private JComboBox cmbDestinatarios;
    private JTextArea txtAreaMensaje;
    private JTextField txtTitulo;
    private JButton btnEnviar;
    private Multipanel panelPrincipal;
    private ControladorUsuarios controladorUsuarios;
    private ControladorMensajes controladorMensajes;

    public PanelEscrituraMensajes(Multipanel panelPrincipal, ControladorUsuarios controladorUsuarios, ControladorMensajes controladorMensajes) throws SQLException {
        this.panelPrincipal=panelPrincipal;
        this.controladorUsuarios=controladorUsuarios;
        this.controladorMensajes=controladorMensajes;
        pnlEscritura.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                ArrayList<Usuario> listaDestinatarios= null;
                try {
                    listaDestinatarios = generarListadoRemitentes(controladorUsuarios);
                    for (Usuario usuario:listaDestinatarios){
                        cmbDestinatarios.addItem(usuario.getNombre());
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                cmbDestinatarios.removeAllItems();
                txtAreaMensaje.setText("");
                txtTitulo.setText("");
            }
        });

        btnEnviar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String tituloMensaje = txtTitulo.getText();
                String textoDelMensaje = txtAreaMensaje.getText();
                String nombreDestinatario = null;
                try {
                    nombreDestinatario = controladorUsuarios.getIdDeDestinatarioPorNombre((String)cmbDestinatarios.getSelectedItem());
                    controladorMensajes.enviarMensaje(tituloMensaje, textoDelMensaje, nombreDestinatario);
                    mensajeConfirmacionDeEnvio(e);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                panelPrincipal.notificarCambioPanel(Multipanel.PANEL_LECTURA);
            }
        });
    }

    public JPanel getPanel(){
        return pnlEscritura;
    }

    public void mensajeConfirmacionDeEnvio (ActionEvent e){
        JOptionPane.showMessageDialog(SwingUtilities.getRoot((Component) e.getSource()), "El mensaje ha sido enviado con éxito.");
    }

    public ArrayList generarListadoRemitentes(ControladorUsuarios controladorUsuarios) throws SQLException {
        return controladorUsuarios.generarListadoRemitentes();
    }
}
