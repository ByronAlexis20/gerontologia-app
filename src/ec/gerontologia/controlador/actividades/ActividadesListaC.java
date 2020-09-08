package ec.gerontologia.controlador.actividades;

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

import ec.gerontologia.modelo.Actividad;
import ec.gerontologia.modelo.ActividadDAO;
import ec.gerontologia.modelo.Encargado;
import ec.gerontologia.modelo.EncargadoDAO;
import ec.gerontologia.modelo.PeriodoLectivo;
import ec.gerontologia.modelo.PeriodoLectivoDAO;

@SuppressWarnings("serial")
public class ActividadesListaC extends SelectorComposer<Component>{
	@Wire private Window winActividadesLista;
	@Wire private Textbox txtBuscar;
	@Wire private Listbox lstActividad;
	
	private List<Actividad> actividades;
	private Actividad actividadSeleccionado;
	ActividadDAO actividadDAO = new ActividadDAO();
	EncargadoDAO encargadoDAO = new EncargadoDAO();
	PeriodoLectivoDAO periodoDAO = new PeriodoLectivoDAO();
	
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

		if (actividades != null) {
			actividades = null; 
		}
		Integer idPeriodo = 0;
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		if(encargado.size() > 0) {
			List<PeriodoLectivo> periodos = periodoDAO.getPeriodoCentro(encargado.get(0).getCentro().getIdCentro());
			if(periodos.size() > 0) {
				idPeriodo = periodos.get(0).getIdPeriodo();
			}
		}
		
		actividades = actividadDAO.getActividades(txtBuscar.getValue(),idPeriodo);
		lstActividad.setModel(new ListModelList(actividades));
		actividadSeleccionado = null;
	}
	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Actividad", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/actividades/actividadesEditar.zul", winActividadesLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEditar")
	public void editar(){
		if (actividadSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una actividad.");
			return; 
		}
		// Actualiza la instancia antes de enviarla a editar.
		actividadDAO.getEntityManager().refresh(actividadSeleccionado);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Actividad", actividadSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/actividades/actividadesEditar.zul", winActividadesLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEliminar")
    public void eliminar(){
        
        if (actividadSeleccionado == null) {
            Clients.showNotification("Debe seleccionar una actividad.");
            return; 
        }
        
        Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
        	@Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onYes")) {
                    try {
                    	actividadSeleccionado.setEstado("I");
                    	actividadDAO.getEntityManager().getTransaction().begin();
                    	actividadDAO.getEntityManager().merge(actividadSeleccionado);
                    	actividadDAO.getEntityManager().getTransaction().commit();;
                        Clients.showNotification("Transaccion ejecutada con exito.");
                        buscar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        actividadDAO.getEntityManager().getTransaction().rollback();
                    }
                }
            }
		});        
    }

	public List<Actividad> getActividades() {
		return actividades;
	}

	public void setActividades(List<Actividad> actividades) {
		this.actividades = actividades;
	}

	public Actividad getActividadSeleccionado() {
		return actividadSeleccionado;
	}

	public void setActividadSeleccionado(Actividad actividadSeleccionado) {
		this.actividadSeleccionado = actividadSeleccionado;
	}
	

}
