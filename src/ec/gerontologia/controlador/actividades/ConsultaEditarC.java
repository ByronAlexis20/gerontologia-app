package ec.gerontologia.controlador.actividades;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Encargado;
import ec.gerontologia.modelo.EncargadoDAO;
import ec.gerontologia.modelo.Matricula;
import ec.gerontologia.modelo.MatriculaDAO;
import ec.gerontologia.modelo.PeriodoLectivo;
import ec.gerontologia.modelo.PeriodoLectivoDAO;
import ec.gerontologia.modelo.UnidadMedica;
import ec.gerontologia.modelo.UnidadMedicaDAO;
import ec.gerontologia.modelo.VisitaMedica;
import ec.gerontologia.modelo.VisitaMedicaDAO;

@SuppressWarnings("serial")
public class ConsultaEditarC extends SelectorComposer<Component>{
	@Wire private Window winConsultaEditar;
	@Wire private Textbox txtCodigo;
	@Wire private Combobox cboUnidadMedica;
	@Wire private Combobox cboPersona;
	@Wire private Datebox dtpFecha;
	@Wire private Textbox txtDiagnostico;

	private ConsultaListaC consultaListaC;
	private VisitaMedicaDAO consultaDAO = new VisitaMedicaDAO();
	private UnidadMedicaDAO unidadDAO = new UnidadMedicaDAO();
	private MatriculaDAO matriculaDAO = new MatriculaDAO();
	private VisitaMedica consulta;


	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);

		// Recupera la ventana padre.
		consultaListaC = (ConsultaListaC)Executions.getCurrent().getArg().get("VentanaPadre");

		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		consulta = null; 
		if (Executions.getCurrent().getArg().get("Consulta") != null) {
			consulta = (VisitaMedica) Executions.getCurrent().getArg().get("Consulta");
		}else{
			consulta = new VisitaMedica(); 
		}
	}


	public boolean isValidarDatos() {
		try {
			Boolean resultado = true;			
			if(cboUnidadMedica.getSelectedItem() == null) {
				Clients.showNotification("Por favor seleccionar la unidad medica","info",cboUnidadMedica,"end_center",2000);
				return resultado;
			}			
			if(txtDiagnostico.getText() == null) {
				Clients.showNotification("Por favor ingrese el diagnostico","info",txtDiagnostico,"end_center",2000);
				txtDiagnostico.focus();
				return resultado;
			}
			if(dtpFecha.getValue() == null) {
				Clients.showNotification("Por favor registre la fecha","info",dtpFecha,"end_center",2000);
				return resultado;
			}
			return resultado;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	EncargadoDAO encargadoDAO = new EncargadoDAO();
	PeriodoLectivoDAO periodoDAO = new PeriodoLectivoDAO();

	@Listen("onClick=#btnGrabar")
	public void grabar(){
		if (isValidarDatos() == true) {
			EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
				public void onEvent(ClickEvent event) throws Exception {
					if(Messagebox.Button.YES.equals(event.getButton())) {
						if(isValidarDatos() == true) {//graba o modifica
							Integer idPeriodo = 0;
							List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
							if(encargado.size() > 0) {
								List<PeriodoLectivo> periodos = periodoDAO.getPeriodoCentro(encargado.get(0).getCentro().getIdCentro());
								if(periodos.size() > 0) {
									idPeriodo = periodos.get(0).getIdPeriodo();
								}
							}
							// Inicia la transaccion
							consultaDAO.getEntityManager().getTransaction().begin();
							consulta.setEstado("A");
							consulta.setIdPeriodo(idPeriodo);
							UnidadMedica p = (UnidadMedica)cboUnidadMedica.getSelectedItem().getValue();
							consulta.setUnidadMedica(p);
							
							Matricula m = (Matricula)cboPersona.getSelectedItem().getValue();
							consulta.setMatricula(m);
							
							if (consulta.getIdVisita() == null) {
								consultaDAO.getEntityManager().persist(consulta);
							}else{
								consulta = (VisitaMedica) consultaDAO.getEntityManager().merge(consulta);
							}
							// Cierra la transaccion.
							consultaDAO.getEntityManager().getTransaction().commit();
							Clients.showNotification("Proceso Ejecutado con exito.");
							consultaListaC.buscar();			
							salir(); 			
						}else{
							Clients.showNotification("Verifique que los campos esten llenos.");
						}
					}
				}
			};
			Messagebox.show("Desea Grabar los Datos?", "Confirmación", new Messagebox.Button[]{
					Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);
		}
	}
	public List<UnidadMedica> getUnidades() {
		List<UnidadMedica> lista = unidadDAO.getUnidadesMedicas("");
		return lista;
	}
	
	public List<Matricula> getPersonas() {
		Integer idPeriodo = 0;
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		if(encargado.size() > 0) {
			List<PeriodoLectivo> periodos = periodoDAO.getPeriodoCentro(encargado.get(0).getCentro().getIdCentro());
			if(periodos.size() > 0) {
				idPeriodo = periodos.get(0).getIdPeriodo();
			}
		}
		
		List<Matricula> lista = matriculaDAO.getPersonaMatriculados(idPeriodo);
		return lista;
	}
	@Listen("onClick=#btnSalir")
	public void salir(){
		winConsultaEditar.detach();
	}
	public VisitaMedica getConsulta() {
		return consulta;
	}
	public void setConsulta(VisitaMedica consulta) {
		this.consulta = consulta;
	}
	@SuppressWarnings("unused")
	private void showNotify(String msg, Component ref) {
		Clients.showNotification(msg, "info", ref, "end_center", 2000);
	}

}
