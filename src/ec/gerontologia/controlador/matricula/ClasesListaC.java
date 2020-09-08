package ec.gerontologia.controlador.matricula;

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
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Clase;
import ec.gerontologia.modelo.ClasesDAO;
import ec.gerontologia.modelo.Encargado;
import ec.gerontologia.modelo.EncargadoDAO;
import ec.gerontologia.modelo.PeriodoLectivo;
import ec.gerontologia.modelo.PeriodoLectivoDAO;

@SuppressWarnings("serial")
public class ClasesListaC extends SelectorComposer<Component>{
	@Wire private Window winClaseLista;
	@Wire private Textbox txtBuscar;
	@Wire private Listbox lstClases;
	@Wire private Button btnBuscar;
	@Wire private Button btnNuevo;
	@Wire private Button btnEditar;
	@Wire private Button btnEliminar;
	@Wire private Button btnImprimir;
	
	private List<Clase> clases;
	private Clase claseSeleccionado;
	ClasesDAO claseDAO = new ClasesDAO();
	PeriodoLectivoDAO periodoDAO = new PeriodoLectivoDAO();
	EncargadoDAO encargadoDAO = new EncargadoDAO();
	
	Integer idPeriodo = 0;
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		if(encargado.size() == 0) {
			Clients.showNotification("No tiene un centro asignado!");
			btnBuscar.setDisabled(true);
			btnEditar.setDisabled(true);
			btnNuevo.setDisabled(true);
			btnEliminar.setDisabled(true);
			btnImprimir.setDisabled(true);
			return;
		}
		
		List<PeriodoLectivo> periodos = periodoDAO.getPeriodoCentro(encargado.get(0).getCentro().getIdCentro());
		if(periodos.size() == 0) {
			Clients.showNotification("No hay periodos en proceso!");
			btnBuscar.setDisabled(true);
			btnEditar.setDisabled(true);
			btnNuevo.setDisabled(true);
			btnEliminar.setDisabled(true);
			btnImprimir.setDisabled(true);
			return;
		}
		idPeriodo = periodos.get(0).getIdPeriodo();
		buscar();
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){
		System.out.println("entra buscar");
		if (clases != null) {
			clases = null; 
		}
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		if(encargado.size() > 0) {
			List<PeriodoLectivo> periodos = periodoDAO.getPeriodoCentro(encargado.get(0).getCentro().getIdCentro());
			if(periodos.size() > 0) {
				clases = claseDAO.getClasePorPeriodo(idPeriodo,txtBuscar.getText());
			}else
				clases = new ArrayList<Clase>();
		}else
			clases = new ArrayList<Clase>();
		lstClases.setModel(new ListModelList(clases));
		claseSeleccionado = null;
	}
	
	@Listen("onClick=#btnNuevo")
	public void nuevo() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Clase", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/matricula/clasesEditar.zul", winClaseLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEditar")
	public void editar(){
		if (claseSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una clase.");
			return; 
		}
		// Actualiza la instancia antes de enviarla a editar.
		periodoDAO.getEntityManager().refresh(claseSeleccionado);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Clase", claseSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/matricula/clasesEditar.zul", winClaseLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEliminar")
    public void eliminar(){
        if (claseSeleccionado == null) {
            Clients.showNotification("Debe seleccionar una clase.");
            return; 
        }
        
        Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
        	@Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onYes")) {
                    try {
                    	claseSeleccionado.setEstado("I");
                    	periodoDAO.getEntityManager().getTransaction().begin();
                    	periodoDAO.getEntityManager().merge(claseSeleccionado);
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
	
	public List<Clase> getClases() {
		return clases;
	}
	public void setClases(List<Clase> clases) {
		this.clases = clases;
	}
	public Clase getClaseSeleccionado() {
		return claseSeleccionado;
	}
	public void setClaseSeleccionado(Clase claseSeleccionado) {
		this.claseSeleccionado = claseSeleccionado;
	}
}
