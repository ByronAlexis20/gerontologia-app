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

import ec.gerontologia.modelo.Etnia;
import ec.gerontologia.modelo.EtniaDAO;

@SuppressWarnings("serial")
public class IdentidadListaC extends SelectorComposer<Component>{
	@Wire private Window winIdentidadLista;
	@Wire private Textbox txtBuscar;
	@Wire private Listbox lstIdentidad;
	
	private List<Etnia> etnia;
	private Etnia etniaSeleccionado;
	EtniaDAO etniaDAO = new EtniaDAO();
	
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

		if (etnia != null) {
			etnia = null; 
		}
		etnia = etniaDAO.getEtnias(txtBuscar.getValue());
		lstIdentidad.setModel(new ListModelList(etnia));
		etniaSeleccionado = null;
	}
	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Etnia", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/administracion/identidadEditar.zul", winIdentidadLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEditar")
	public void editar(){
		if (etniaSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un perfil.");
			return; 
		}
		// Actualiza la instancia antes de enviarla a editar.
		etniaDAO.getEntityManager().refresh(etniaSeleccionado);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Etnia", etniaSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/administracion/identidadEditar.zul", winIdentidadLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEliminar")
    public void eliminar(){
        
        if (etniaSeleccionado == null) {
            Clients.showNotification("Debe seleccionar una identidad etnica.");
            return; 
        }
        
        Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
        	@Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onYes")) {
                    try {
                    	etniaSeleccionado.setEstado("I");
                    	etniaDAO.getEntityManager().getTransaction().begin();
                    	etniaDAO.getEntityManager().merge(etniaSeleccionado);
                    	etniaDAO.getEntityManager().getTransaction().commit();;
                        Clients.showNotification("Transaccion ejecutada con exito.");
                        buscar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        etniaDAO.getEntityManager().getTransaction().rollback();
                    }
                }
            }
		});        
    }
	public List<Etnia> getEtnia() {
		return etnia;
	}
	public void setEtnia(List<Etnia> etnia) {
		this.etnia = etnia;
	}
	public Etnia getEtniaSeleccionado() {
		return etniaSeleccionado;
	}
	public void setEtniaSeleccionado(Etnia etniaSeleccionado) {
		this.etniaSeleccionado = etniaSeleccionado;
	}

}
