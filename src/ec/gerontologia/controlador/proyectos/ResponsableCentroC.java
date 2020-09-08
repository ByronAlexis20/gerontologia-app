package ec.gerontologia.controlador.proyectos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Centro;
import ec.gerontologia.modelo.Encargado;
import ec.gerontologia.modelo.EncargadoDAO;

@SuppressWarnings("serial")
public class ResponsableCentroC extends SelectorComposer<Component>{
	@Wire private Window winRegistroResponsables;
	@Wire private Textbox txtIdCentro;
	@Wire private Textbox txtDireccion;
	@Wire private Textbox txtNombreCentro;
	@Wire private Listbox lstEncargados;
	
	private Centro centro;
	private Encargado encargadoSeleccionado;
	private List<Encargado> encargado;
	
	EncargadoDAO encargadoDAO = new EncargadoDAO();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		
	}
	
	@Listen("onClick=#btnBuscarCentro")
	public void buscarCentro() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/proyectos/seleccionarCentro.zul", winRegistroResponsables, params);
		ventanaCargar.doModal();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void recuperarCentro(Centro centroRecuperado) {
		if(centroRecuperado != null) {//recupera el centro
			centro = centroRecuperado;
			txtDireccion.setText(centro.getDireccion());
			txtIdCentro.setText(String.valueOf(centro.getIdCentro()));
			txtNombreCentro.setText(centro.getNombreCentro());
			recuperarResponsables(centroRecuperado);
		}else {//no recupera nada
			centro = null;
			txtDireccion.setText("");
			txtIdCentro.setText("");
			txtNombreCentro.setText("");
			List<Encargado> lista = new ArrayList<Encargado>();
			lstEncargados.setModel(new ListModelList(lista));
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void recuperarResponsables(Centro centroRecuperado) {
		List<Encargado> encargadoCentro = encargadoDAO.getEncargadosCentro(centroRecuperado.getIdCentro());
		if(encargadoCentro.size() > 0) {
			lstEncargados.setModel(new ListModelList(encargadoCentro));
		}else {
			List<Encargado> lista = new ArrayList<Encargado>();
			lstEncargados.setModel(new ListModelList(lista));
		}
			
	}
	
	@Listen("onClick=#btnAgregar")
	public void agregarResponsable() {
		if(centro == null) {
			Clients.showNotification("Debe seleccionar un centro");
			return;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("VentanaPadre", this);
		params.put("Centro", centro);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/proyectos/listaUsuarios.zul", winRegistroResponsables, params);
		ventanaCargar.doModal();
	}
	
	public Centro getCentro() {
		return centro;
	}

	public void setCentro(Centro centro) {
		this.centro = centro;
	}

	public Encargado getEncargadoSeleccionado() {
		return encargadoSeleccionado;
	}

	public void setEncargadoSeleccionado(Encargado encargadoSeleccionado) {
		this.encargadoSeleccionado = encargadoSeleccionado;
	}

	public List<Encargado> getEncargado() {
		return encargado;
	}

	public void setEncargado(List<Encargado> encargado) {
		this.encargado = encargado;
	}
	
	
}
