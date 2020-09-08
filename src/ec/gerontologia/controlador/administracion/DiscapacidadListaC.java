package ec.gerontologia.controlador.administracion;

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

import ec.gerontologia.modelo.Discapacidad;
import ec.gerontologia.modelo.DiscapacidadDAO;

@SuppressWarnings("serial")
public class DiscapacidadListaC extends SelectorComposer<Component>{
	@Wire private Window winDiscapacidadLista;
	@Wire private Textbox txtBuscar;
	@Wire private Listbox lstDiscapacidad;
	
	private List<Discapacidad> discapacidad;
	private Discapacidad discapacidadSeleccionado;
	DiscapacidadDAO discapacidadDAO = new DiscapacidadDAO();
	
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

		if (discapacidad != null) {
			discapacidad = null; 
		}
		discapacidad = discapacidadDAO.getDiscapacidades(txtBuscar.getValue());
		lstDiscapacidad.setModel(new ListModelList(discapacidad));
		discapacidadSeleccionado = null;
	}
	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Instruccion", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/administracion/discapacidadEditar.zul", winDiscapacidadLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEditar")
	public void editar(){
		if (discapacidadSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una discapacidad.");
			return; 
		}
		// Actualiza la instancia antes de enviarla a editar.
		discapacidadDAO.getEntityManager().refresh(discapacidadSeleccionado);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Discapacidad", discapacidadSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/administracion/discapacidadEditar.zul", winDiscapacidadLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEliminar")
    public void eliminar(){
        
        if (discapacidadSeleccionado == null) {
            Clients.showNotification("Debe seleccionar una discapacidad.");
            return; 
        }
        
        Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
        	@Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onYes")) {
                    try {
                    	discapacidadSeleccionado.setEstado("I");
                    	discapacidadDAO.getEntityManager().getTransaction().begin();
                    	discapacidadDAO.getEntityManager().merge(discapacidadSeleccionado);
                    	discapacidadDAO.getEntityManager().getTransaction().commit();;
                        Clients.showNotification("Transaccion ejecutada con exito.");
                        buscar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        discapacidadDAO.getEntityManager().getTransaction().rollback();
                    }
                }
            }
		});        
    }
	public List<Discapacidad> getDiscapacidad() {
		return discapacidad;
	}
	public void setDiscapacidad(List<Discapacidad> discapacidad) {
		this.discapacidad = discapacidad;
	}
	public Discapacidad getDiscapacidadSeleccionado() {
		return discapacidadSeleccionado;
	}
	public void setDiscapacidadSeleccionado(Discapacidad discapacidadSeleccionado) {
		this.discapacidadSeleccionado = discapacidadSeleccionado;
	}
}
