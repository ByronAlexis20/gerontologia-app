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

import ec.gerontologia.modelo.Encargado;
import ec.gerontologia.modelo.EncargadoDAO;
import ec.gerontologia.modelo.PeriodoLectivo;
import ec.gerontologia.modelo.PeriodoLectivoDAO;
import ec.gerontologia.modelo.VisitaMedica;
import ec.gerontologia.modelo.VisitaMedicaDAO;

@SuppressWarnings("serial")
public class ConsultaListaC extends SelectorComposer<Component>{
	@Wire private Window winConsultaLista;
	@Wire private Textbox txtBuscar;
	@Wire private Listbox lstConsulta;
	
	private List<VisitaMedica> consultas;
	private VisitaMedica consultaSeleccionado;
	VisitaMedicaDAO consultaDAO = new VisitaMedicaDAO();
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

		if (consultas != null) {
			consultas = null; 
		}
		Integer idPeriodo = 0;
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		if(encargado.size() > 0) {
			List<PeriodoLectivo> periodos = periodoDAO.getPeriodoCentro(encargado.get(0).getCentro().getIdCentro());
			if(periodos.size() > 0) {
				idPeriodo = periodos.get(0).getIdPeriodo();
			}
		}
		consultas = consultaDAO.getVisitas(idPeriodo);
		lstConsulta.setModel(new ListModelList(consultas));
		consultaSeleccionado = null;
	}
	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Consulta", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/actividades/consultaEditar.zul", winConsultaLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEditar")
	public void editar(){
		if (consultaSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una consulta.");
			return; 
		}
		// Actualiza la instancia antes de enviarla a editar.
		consultaDAO.getEntityManager().refresh(consultaSeleccionado);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Consulta", consultaSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/actividades/consultaEditar.zul", winConsultaLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnEliminar")
    public void eliminar(){
        
        if (consultaSeleccionado == null) {
            Clients.showNotification("Debe seleccionar una consulta.");
            return; 
        }
        
        Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener<Event>() {
        	@Override
            public void onEvent(Event event) throws Exception {
                if (event.getName().equals("onYes")) {
                    try {
                    	consultaSeleccionado.setEstado("I");
                    	consultaDAO.getEntityManager().getTransaction().begin();
                    	consultaDAO.getEntityManager().merge(consultaSeleccionado);
                    	consultaDAO.getEntityManager().getTransaction().commit();;
                        Clients.showNotification("Transaccion ejecutada con exito.");
                        buscar();
                    } catch (Exception e) {
                        e.printStackTrace();
                        consultaDAO.getEntityManager().getTransaction().rollback();
                    }
                }
            }
		});        
    }

	public List<VisitaMedica> getConsultas() {
		return consultas;
	}

	public void setConsultas(List<VisitaMedica> consultas) {
		this.consultas = consultas;
	}

	public VisitaMedica getConsultaSeleccionado() {
		return consultaSeleccionado;
	}

	public void setConsultaSeleccionado(VisitaMedica consultaSeleccionado) {
		this.consultaSeleccionado = consultaSeleccionado;
	}

	

}
