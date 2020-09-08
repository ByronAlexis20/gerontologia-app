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

import ec.gerontologia.modelo.Instruccion;
import ec.gerontologia.modelo.InstruccionDAO;

@SuppressWarnings("serial")
public class InstruccionListaC extends SelectorComposer<Component>{
	@Wire private Window winInstruccionLista;
	@Wire private Textbox txtBuscar;
	@Wire private Listbox lstInstruccion;
	
	private List<Instruccion> instruccion;
	private Instruccion instruccionSeleccionado;
	InstruccionDAO instruccionDAO = new InstruccionDAO();
	
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

		if (instruccion != null) {
			instruccion = null; 
		}
		instruccion = instruccionDAO.getInstrucciones(txtBuscar.getValue());
		lstInstruccion.setModel(new ListModelList(instruccion));
		instruccionSeleccionado = null;
	}
	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Instruccion", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/administracion/instruccionEditar.zul", winInstruccionLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEditar")
	public void editar(){
		if (instruccionSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un perfil.");
			return; 
		}
		// Actualiza la instancia antes de enviarla a editar.
		instruccionDAO.getEntityManager().refresh(instruccionSeleccionado);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Instruccion", instruccionSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/administracion/instruccionEditar.zul", winInstruccionLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEliminar")
    public void eliminar(){
        
        if (instruccionSeleccionado == null) {
            Clients.showNotification("Debe seleccionar una instrucción.");
            return; 
        }
        
        Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
        	@Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onYes")) {
                    try {
                    	instruccionSeleccionado.setEstado("I");
                    	instruccionDAO.getEntityManager().getTransaction().begin();
                    	instruccionDAO.getEntityManager().merge(instruccionSeleccionado);
                    	instruccionDAO.getEntityManager().getTransaction().commit();;
                        Clients.showNotification("Transaccion ejecutada con exito.");
                        buscar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        instruccionDAO.getEntityManager().getTransaction().rollback();
                    }
                }
            }
		});        
    }
	public List<Instruccion> getInstruccion() {
		return instruccion;
	}
	public void setInstruccion(List<Instruccion> instruccion) {
		this.instruccion = instruccion;
	}
	public Instruccion getInstruccionSeleccionado() {
		return instruccionSeleccionado;
	}
	public void setInstruccionSeleccionado(Instruccion instruccionSeleccionado) {
		this.instruccionSeleccionado = instruccionSeleccionado;
	}
}
