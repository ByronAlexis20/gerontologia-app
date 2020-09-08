package ec.gerontologia.controlador.proyectos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Centro;
import ec.gerontologia.modelo.CentroDAO;

@SuppressWarnings("serial")
public class CentrosListaC extends SelectorComposer<Component>{
	@Wire private Window winCentrosLista;
	@Wire private Textbox txtBuscar;
	@Wire private Listbox lstCentro;
	
	private List<Centro> centro;
	private Centro centroSeleccionado;
	CentroDAO centroDAO = new CentroDAO();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		buscar();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){
		System.out.println("entra buscar");

		if (centro != null) {
			centro = null; 
		}
		centro = centroDAO.getCentros(txtBuscar.getValue());
		lstCentro.setModel(new ListModelList(centro));
		centroSeleccionado = null;
	}
	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Centro", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/proyectos/centrosEditar.zul", winCentrosLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEditar")
	public void editar(){
		if (centroSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un centro.");
			return; 
		}
		// Actualiza la instancia antes de enviarla a editar.
		centroDAO.getEntityManager().refresh(centroSeleccionado);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Centro", centroSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/proyectos/centrosEditar.zul", winCentrosLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEliminar")
    public void eliminar(){
        
        if (centroSeleccionado == null) {
            Clients.showNotification("Debe seleccionar un centro.");
            return; 
        }
        
        Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
        	@Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onYes")) {
                    try {
                    	centroSeleccionado.setEstado("I");
                    	centroDAO.getEntityManager().getTransaction().begin();
                    	centroDAO.getEntityManager().merge(centroSeleccionado);
                    	centroDAO.getEntityManager().getTransaction().commit();;
                        Clients.showNotification("Transaccion ejecutada con exito.");
                        buscar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        centroDAO.getEntityManager().getTransaction().rollback();
                    }
                }
            }
		});        
    }
	public List<Centro> getCentro() {
		return centro;
	}
	public void setCentro(List<Centro> centro) {
		this.centro = centro;
	}
	public Centro getCentroSeleccionado() {
		return centroSeleccionado;
	}
	public void setCentroSeleccionado(Centro centroSeleccionado) {
		this.centroSeleccionado = centroSeleccionado;
	}
}
