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

import ec.gerontologia.modelo.UnidadMedica;
import ec.gerontologia.modelo.UnidadMedicaDAO;

@SuppressWarnings("serial")
public class UnidadListaC extends SelectorComposer<Component>{
	@Wire private Window winUnidadLista;
	@Wire private Textbox txtBuscar;
	@Wire private Listbox lstUnidad;
	
	private List<UnidadMedica> unidad;
	private UnidadMedica unidadSeleccionado;
	UnidadMedicaDAO unidadDAO = new UnidadMedicaDAO();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		buscar();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){
		if (unidad != null) {
			unidad = null; 
		}
		unidad = unidadDAO.getUnidadesMedicas(txtBuscar.getValue());
		lstUnidad.setModel(new ListModelList(unidad));
		unidadSeleccionado = null;
	}
	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Unidad", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/administracion/unidadEditar.zul", winUnidadLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEditar")
	public void editar(){
		if (unidadSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una unidad medica.");
			return; 
		}
		// Actualiza la instancia antes de enviarla a editar.
		unidadDAO.getEntityManager().refresh(unidadSeleccionado);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Unidad", unidadSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/administracion/unidadEditar.zul", winUnidadLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEliminar")
    public void eliminar(){
        
        if (unidadSeleccionado == null) {
            Clients.showNotification("Debe seleccionar un servicio.");
            return; 
        }
        
        Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
        	@Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onYes")) {
                    try {
                    	unidadSeleccionado.setEstado("I");
                    	unidadDAO.getEntityManager().getTransaction().begin();
                    	unidadDAO.getEntityManager().merge(unidadSeleccionado);
                    	unidadDAO.getEntityManager().getTransaction().commit();;
                        Clients.showNotification("Transaccion ejecutada con exito.");
                        buscar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        unidadDAO.getEntityManager().getTransaction().rollback();
                    }
                }
            }
		});        
    }
	public List<UnidadMedica> getUnidad() {
		return unidad;
	}
	public void setUnidad(List<UnidadMedica> unidad) {
		this.unidad = unidad;
	}
	public UnidadMedica getUnidadSeleccionado() {
		return unidadSeleccionado;
	}
	public void setUnidadSeleccionado(UnidadMedica unidadSeleccionado) {
		this.unidadSeleccionado = unidadSeleccionado;
	}

}
