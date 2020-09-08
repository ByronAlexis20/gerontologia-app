package ec.gerontologia.controlador.periodo;

import java.util.ArrayList;
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

import ec.gerontologia.modelo.Encargado;
import ec.gerontologia.modelo.EncargadoDAO;
import ec.gerontologia.modelo.PeriodoLectivo;
import ec.gerontologia.modelo.PeriodoLectivoDAO;

@SuppressWarnings("serial")
public class PeriodoListaC extends SelectorComposer<Component>{
	@Wire private Window winPeriodoLista;
	@Wire private Textbox txtBuscar;
	@Wire private Listbox lstPeriodo;
	
	private List<PeriodoLectivo> periodo;
	private PeriodoLectivo periodoSeleccionado;
	PeriodoLectivoDAO periodoDAO = new PeriodoLectivoDAO();
	EncargadoDAO encargadoDAO = new EncargadoDAO();
	
	
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
		if (periodo != null) {
			periodo = null; 
		}
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		if(encargado.size() > 0) {
			periodo = periodoDAO.getPeriodoCentro(encargado.get(0).getCentro().getIdCentro());
		}else
			periodo = new ArrayList<PeriodoLectivo>();
		lstPeriodo.setModel(new ListModelList(periodo));
		periodoSeleccionado = null;
	}
	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		if(encargado.size() == 0) {
			Clients.showNotification("No tiene un centro asignado!");
			return;
		}
		
		List<PeriodoLectivo> periodos = periodoDAO.getPeriodoCentroEnProceso(encargado.get(0).getCentro().getIdCentro());
		if(periodos.size() > 0) {
			Clients.showNotification("Existe un periodo en proceso, no puede iniciar otro!");
			return;
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Periodo", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/periodo/periodoEditar.zul", winPeriodoLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEditar")
	public void editar(){
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		if(encargado.size() == 0) {
			Clients.showNotification("No tiene un centro asignado!");
			return;
		}
		
		if (periodoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un periodo.");
			return; 
		}
		// Actualiza la instancia antes de enviarla a editar.
		periodoDAO.getEntityManager().refresh(periodoSeleccionado);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Periodo", periodoSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/periodo/periodoEditar.zul", winPeriodoLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEliminar")
    public void eliminar(){
        
        if (periodoSeleccionado == null) {
            Clients.showNotification("Debe seleccionar un periodo.");
            return; 
        }
        
        Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
        	@Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onYes")) {
                    try {
                    	periodoSeleccionado.setEstado("I");
                    	periodoDAO.getEntityManager().getTransaction().begin();
                    	periodoDAO.getEntityManager().merge(periodoSeleccionado);
                    	periodoDAO.getEntityManager().getTransaction().commit();;
                        Clients.showNotification("Transaccion ejecutada con exito.");
                        buscar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        periodoDAO.getEntityManager().getTransaction().rollback();
                    }
                }
            }
		});        
    }
	@Listen("onClick=#btnFinalizar")
	public void finalizarPeriodo(){
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		if(encargado.size() == 0) {
			Clients.showNotification("No tiene un centro asignado!");
			return;
		}
		if (periodoSeleccionado == null) {
			Clients.showNotification("Debe seleccionar un periodo.");
			return; 
		}
		// Actualiza la instancia antes de enviarla a editar.
		periodoDAO.getEntityManager().refresh(periodoSeleccionado);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Periodo", periodoSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/periodo/finalizarPeriodo.zul", winPeriodoLista, params);
		ventanaCargar.doModal();
	}
	public List<PeriodoLectivo> getPeriodo() {
		return periodo;
	}
	public void setPeriodo(List<PeriodoLectivo> periodo) {
		this.periodo = periodo;
	}
	public PeriodoLectivo getPeriodoSeleccionado() {
		return periodoSeleccionado;
	}
	public void setPeriodoSeleccionado(PeriodoLectivo periodoSeleccionado) {
		this.periodoSeleccionado = periodoSeleccionado;
	}
	
}
