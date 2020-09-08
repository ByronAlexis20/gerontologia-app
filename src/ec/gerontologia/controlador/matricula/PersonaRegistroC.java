package ec.gerontologia.controlador.matricula;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.gerontologia.modelo.Canton;
import ec.gerontologia.modelo.CantonDAO;
import ec.gerontologia.modelo.Discapacidad;
import ec.gerontologia.modelo.DiscapacidadDAO;
import ec.gerontologia.modelo.Encargado;
import ec.gerontologia.modelo.EncargadoDAO;
import ec.gerontologia.modelo.EstadoVivienda;
import ec.gerontologia.modelo.EstadoViviendaDAO;
import ec.gerontologia.modelo.Etnia;
import ec.gerontologia.modelo.EtniaDAO;
import ec.gerontologia.modelo.Instruccion;
import ec.gerontologia.modelo.InstruccionDAO;
import ec.gerontologia.modelo.Matricula;
import ec.gerontologia.modelo.MatriculaDAO;
import ec.gerontologia.modelo.Parroquia;
import ec.gerontologia.modelo.ParroquiaDAO;
import ec.gerontologia.modelo.PeriodoLectivo;
import ec.gerontologia.modelo.PeriodoLectivoDAO;
import ec.gerontologia.modelo.Persona;
import ec.gerontologia.modelo.PersonaDAO;
import ec.gerontologia.modelo.Provincia;
import ec.gerontologia.modelo.ProvinciaDAO;
import ec.gerontologia.modelo.Representante;
import ec.gerontologia.modelo.RepresentanteDAO;
import ec.gerontologia.modelo.Salud;
import ec.gerontologia.modelo.SaludDAO;
import ec.gerontologia.modelo.Servicio;
import ec.gerontologia.modelo.ServicioDAO;
import ec.gerontologia.modelo.Vivienda;
import ec.gerontologia.modelo.ViviendaDAO;

@SuppressWarnings("serial")
public class PersonaRegistroC extends SelectorComposer<Component>{
	
	ProvinciaDAO provinciaDAO = new ProvinciaDAO();
	CantonDAO cantonDAO = new CantonDAO();
	ParroquiaDAO parroquiaDAO = new ParroquiaDAO();
	
	@Wire Window winRegistroPersonas;
	@Wire private Button btnRegistrar;
	@Wire private Button btnNuevo;
	@Wire private Textbox txtCedula;
	@Wire private Textbox txtNombres;
	@Wire private Textbox txtTelefono;
	@Wire private Combobox cboGenero;
	@Wire private Textbox txtCelular;
	@Wire private Textbox txtApellidos;
	@Wire private Datebox dtpFecha;
	@Wire private Textbox txtEmail;
	@Wire private Textbox txtDireccion;
	@Wire private Combobox cboServicio;
	@Wire private Combobox cboEtnia;
	@Wire private Combobox cboDiscapacidad;
	@Wire private Textbox txtPorcentajeDiscapacidad;
	@Wire private Combobox cboInstruccion;
	@Wire private Combobox cboEstadoSalud;
	@Wire private Combobox cboTipoVivienda;
	@Wire private Combobox cboEstadoVivienda;
	@Wire private Combobox cboProvincia;
	@Wire private Combobox cboCanton;
	@Wire private Combobox cboParroquia;
	
	//componentes para el representante
	@Wire private Textbox txtNombresRep;
	@Wire private Textbox txtCedulaRep;
	@Wire private Textbox txtTelefonoRep;
	@Wire private Combobox cboGeneroRep;
	@Wire private Textbox txtCelularRep;
	@Wire private Textbox txtApellidosRep;
	@Wire private Textbox txtEmailRep;
	@Wire private Textbox txtDireccionRep;
	@Wire private Textbox txtIngresoMensual;
	@Wire private Datebox dtpFechaRep;
	
	@Wire private Combobox cboProvinciaRep;
	@Wire private Combobox cboCantonRep;
	@Wire private Combobox cboParroquiaRep;
	
	ServicioDAO servicioDAO = new ServicioDAO();
	private Servicio servicioSeleccionado;
	EtniaDAO etniaDAO = new EtniaDAO();
	private Etnia etniaSeleccionado;
	DiscapacidadDAO discapacidadDAO = new DiscapacidadDAO();
	private Discapacidad discapacidadSeleccionado;
	InstruccionDAO instruccionDAO = new InstruccionDAO();
	private Instruccion instruccionSeleccionado;
	SaludDAO saludDAO = new SaludDAO();
	private Salud estadoSaludSeleccionado;
	ViviendaDAO viviendaDAO = new ViviendaDAO();
	private Vivienda viviendaSeleccionado;
	EstadoViviendaDAO estadoViviendaDAO = new EstadoViviendaDAO();
	private EstadoVivienda estadoViviendaSeleccionado;
	
	private Provincia provinciaSeleccionado;
	private Canton cantonSeleccionado;
	private Parroquia parroquiaSeleccionado;
	
	private Provincia provinciaSeleccionadoRep;
	private Canton cantonSeleccionadoRep;
	private Parroquia parroquiaSeleccionadoRep;
	
	List<Canton> cantones;
	List<Parroquia> parroquias;
	
	List<Canton> cantonesRep;
	List<Parroquia> parroquiasRep;
	
	PersonaDAO personaDAO = new PersonaDAO();
	RepresentanteDAO representanteDAO = new RepresentanteDAO();
	Persona personaRecuperada;
	Representante representanteRecuperado;
	
	Integer idCentro = 0;
	EncargadoDAO encargadoDAO = new EncargadoDAO();
	MatriculaDAO matriculaDAO = new MatriculaDAO();
	PeriodoLectivoDAO periodoDAO = new PeriodoLectivoDAO();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		List<Encargado> encargado = encargadoDAO.getEncargadosUsuario();
		if(encargado.size() > 0)
			idCentro = encargado.get(0).getCentro().getIdCentro();
		
		if(idCentro == 0) {
			Clients.showNotification("El usuario no tiene centros asignados");
			bloquearComponentes();
		}
		
		List<PeriodoLectivo> listaPeriodo = periodoDAO.getPeriodoCentroEnProceso(idCentro);
		
		if(listaPeriodo.size() == 0) {
			Clients.showNotification("No existend periodos activos en el centro, por lo tanto no se puede matricular a personas");
			bloquearComponentes();
		}
	}

	Persona personaGrabar;
	Representante representanteGrabar;
	
	@Listen("onClick=#btnRegistrar")
	public void grabarRegistro() {
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
            public void onEvent(ClickEvent event) throws Exception {
                if(Messagebox.Button.YES.equals(event.getButton())) {
                	if(validarDatos() == true) {//graba o modifica
                		List<Persona> listaPersonas = personaDAO.getPersonaByCedula(txtCedula.getText());
                		if(listaPersonas.size() > 0)
                			personaGrabar = listaPersonas.get(0);
                		else
                			personaGrabar = new Persona();
                		
                		
                		//antes de grabar la matricula hay q recuperar el perido lectivo
                		List<PeriodoLectivo> listaPeriodo = periodoDAO.getPeriodoCentroEnProceso(idCentro);
                		
                		//luego se llenan todos los datos en el objeto
                		llenarDatosPersona();
                		Matricula matricula = new Matricula();
                		matricula.setEstado("A");
                		matricula.setFecha(new Date());
                		matricula.setIdMatricula(null);
                		matricula.setObservacion("NINGUNA OBSERVACIÓN");
                		matricula.setPeriodoLectivo(listaPeriodo.get(0));
                		matricula.setPersona(personaGrabar);
                		
                		if (personaGrabar.getMatriculas() != null)
                			personaGrabar.addMatricula(matricula);
                		else {
                			List<Matricula> listaMatriculaGrabar = new ArrayList<Matricula>();
                			listaMatriculaGrabar.add(matricula);
                			personaGrabar.setMatriculas(listaMatriculaGrabar);
                		}
                		//luego el representante
                		List<Representante> listaRepresentante = representanteDAO.getRepresentanteByCedula(txtCedulaRep.getText());
                		if(listaRepresentante.size() > 0) 
                			representanteGrabar = listaRepresentante.get(0);
                		else
                			representanteGrabar = new Representante();
                		//copiar los datos del representante
                		llenarDatosRepresetante();
                		matricula.setRepresentante(representanteGrabar);
                		if(representanteGrabar.getIdRepresentante() == null) {
                			List<Matricula> listaMatriculaGrabar = new ArrayList<Matricula>();
                			listaMatriculaGrabar.add(matricula);
                			representanteGrabar.setMatriculas(listaMatriculaGrabar);
                		}else {
                			representanteGrabar.addMatricula(matricula);
                		}
                		
                		matriculaDAO.getEntityManager().getTransaction().begin();
                		if(personaGrabar.getIdPersona() != null)
                			matriculaDAO.getEntityManager().persist(matricula);
                		else {
                			matriculaDAO.getEntityManager().merge(personaGrabar);
                			//matriculaDAO.getEntityManager().persist(matricula);
                		}
                		matriculaDAO.getEntityManager().getTransaction().commit();
                		Clients.showNotification("Persona registrada con exito");
                		
                		nuevo();
                	}
                }
            }
		};
		Messagebox.show("Desea Grabar los Datos?", "Confirmación", new Messagebox.Button[]{
                Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);
	}
	
	private void llenarDatosPersona() {
		personaGrabar.setApellido(txtApellidos.getText());
		personaGrabar.setCedula(txtCedula.getText());
		personaGrabar.setCelular(txtCelular.getText());
		personaGrabar.setDiscapacidad((Discapacidad)cboDiscapacidad.getSelectedItem().getValue());
		personaGrabar.setEmail(txtEmail.getText());
		personaGrabar.setEstado("A");
		personaGrabar.setEstadoVivienda((EstadoVivienda)cboEstadoVivienda.getSelectedItem().getValue());
		personaGrabar.setEtnia((Etnia)cboEtnia.getSelectedItem().getValue());
		personaGrabar.setFechaNacimiento(dtpFecha.getValue());
		personaGrabar.setGenero((String)cboGenero.getSelectedItem().getValue());
		personaGrabar.setInstruccion((Instruccion)cboInstruccion.getSelectedItem().getValue());
		personaGrabar.setNombre(txtNombres.getText());
		personaGrabar.setParroquia((Parroquia)cboParroquia.getSelectedItem().getValue());
		personaGrabar.setProcentajeDiscapacidad(Integer.parseInt(txtPorcentajeDiscapacidad.getText()));
		personaGrabar.setReferenciaDomicilio(txtDireccion.getText());
		personaGrabar.setSalud((Salud)cboEstadoSalud.getSelectedItem().getValue());
		personaGrabar.setServicio((Servicio)cboServicio.getSelectedItem().getValue());
		personaGrabar.setTelefono(txtTelefono.getText());
		personaGrabar.setVivienda((Vivienda)cboTipoVivienda.getSelectedItem().getValue());
	}
	
	private void llenarDatosRepresetante() {
		representanteGrabar.setApellido(txtApellidosRep.getText());
		representanteGrabar.setCedula(txtCedulaRep.getText());
		representanteGrabar.setCelular(txtCelularRep.getText());
		representanteGrabar.setEmail(txtEmailRep.getText());
		representanteGrabar.setEstado("A");
		representanteGrabar.setFechaNacimiento(dtpFechaRep.getValue());
		representanteGrabar.setGenero((String)cboGeneroRep.getSelectedItem().getValue());
		representanteGrabar.setIngresoMensual(Double.parseDouble(txtIngresoMensual.getText()));
		representanteGrabar.setNombre(txtNombresRep.getText());
		representanteGrabar.setParroquia((Parroquia)cboParroquiaRep.getSelectedItem().getValue());
		representanteGrabar.setReferenciaDomicilio(txtDireccionRep.getText());
		representanteGrabar.setTelefono(txtTelefonoRep.getText());
	}
	
	private boolean validarDatos() {
		try {
			if(txtCedula.getText() == "") {
				Clients.showNotification("Debe registrar cédula de la persona","info",txtCedula,"end_center",2000);
				txtCedula.focus();
				return false;
			}
			if(validarDeCedula(txtCedula.getText())== false) {
				Clients.showNotification("La cédula ingresada es incorrecta","info",txtCedula,"end_center",2000);
				txtCedula.focus();
				return false;
			}
			if(txtNombres.getText() == "") {
				Clients.showNotification("Debe registrar nombres de la persona","info",txtNombres,"end_center",2000);
				txtNombres.focus();
				return false;
			}
			if(txtApellidos.getText() == "") {
				Clients.showNotification("Debe registrar apellidos de la persona","info",txtApellidos,"end_center",2000);
				txtApellidos.focus();
				return false;
			}
			if(cboGenero.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar género de la persona","info",cboGenero,"end_center",2000);
				return false;
			}
			if(dtpFecha.getValue() == null) {
				Clients.showNotification("Debe registrar fecha de nacimiento de la persona","info",dtpFecha,"end_center",2000);
				return false;
			}
			if(cboParroquia.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar parroquia de residencia de la persona","info",cboParroquia,"end_center",2000);
				return false;
			}
			if(cboServicio.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar servicio","info",cboServicio,"end_center",2000);
				return false;
			}
			if(cboEtnia.getSelectedIndex() == -1) {
				Clients.showNotification("Debe identidad etnica de la persona","info",cboEtnia,"end_center",2000);
				return false;
			}
			if(cboDiscapacidad.getSelectedIndex() == -1) {
				Clients.showNotification("Debe identidad el tipo de discapacidad de la persona","info",cboDiscapacidad,"end_center",2000);
				return false;
			}
			if(txtPorcentajeDiscapacidad.getText() == "") {
				Clients.showNotification("Debe registrar el procentaje de discapacidad de la persona","info",txtPorcentajeDiscapacidad,"end_center",2000);
				txtPorcentajeDiscapacidad.focus();
				return false;
			}
			if(cboInstruccion.getSelectedIndex() == -1) {
				Clients.showNotification("Debe identidad el nivel de instruccion de la persona","info",cboInstruccion,"end_center",2000);
				return false;
			}
			if(cboEstadoSalud.getSelectedIndex() == -1) {
				Clients.showNotification("Debe identidad el estado de salud de la persona","info",cboEstadoSalud,"end_center",2000);
				return false;
			}
			if(cboTipoVivienda.getSelectedIndex() == -1) {
				Clients.showNotification("Debe identidad el tipo de vivienda de la persona","info",cboTipoVivienda,"end_center",2000);
				return false;
			}
			if(cboEstadoVivienda.getSelectedIndex() == -1) {
				Clients.showNotification("Debe identidad el estado de la vivienda de la persona","info",cboEstadoVivienda,"end_center",2000);
				return false;
			}
			if(txtCedulaRep.getText() == "") {
				Clients.showNotification("Debe registrar número de cedula del representante","info",txtCedulaRep,"end_center",2000);
				txtCedulaRep.focus();
				return false;
			}
			if(txtNombresRep.getText() == "") {
				Clients.showNotification("Debe registrar el nombre del representante","info",txtNombresRep,"end_center",2000);
				txtNombresRep.focus();
				return false;
			}
			if(txtApellidosRep.getText() == "") {
				Clients.showNotification("Debe registrar el apellido del representante","info",txtApellidosRep,"end_center",2000);
				txtApellidosRep.focus();
				return false;
			}
			if(cboGeneroRep.getSelectedIndex() == -1) {
				Clients.showNotification("Debe identidad el genero del representante","info",cboGeneroRep,"end_center",2000);
				return false;
			}
			if(dtpFechaRep.getValue() == null) {
				Clients.showNotification("Debe registrar fecha de nacimiento del representante","info",dtpFechaRep,"end_center",2000);
				return false;
			}
			if(cboParroquiaRep.getSelectedIndex() == -1) {
				Clients.showNotification("Debe identidad la parroquia de residencia del representante","info",cboParroquiaRep,"end_center",2000);
				return false;
			}
			if(txtIngresoMensual.getText() == "") {
				Clients.showNotification("Debe registrar el ingreso mensual del representante","info",txtIngresoMensual,"end_center",2000);
				txtIngresoMensual.focus();
				return false;
			}
			if(txtEmail.getText() == "") {
				if(ValidarEmail(txtEmail.getText()) == false) {
					Clients.showNotification("El correo ingresado es inválido","info",txtEmail,"end_center",2000);
					txtEmail.focus();
					return false;
				}
			}
			if(txtEmailRep.getText() == "") {
				if(ValidarEmail(txtEmailRep.getText()) == false) {
					Clients.showNotification("El correo ingresado es inválido","info",txtEmailRep,"end_center",2000);
					txtEmailRep.focus();
					return false;
				}
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	@Listen("onClick=#btnNuevo")
	public void nuevo() {
		txtCedula.setText("");
		limpiarPersona();
		limpiarRepresentante();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void limpiarPersona() {
		txtNombres.setText("");
		txtTelefono.setText("");
		cboGenero.setText("");
		txtCelular.setText("");
		txtApellidos.setText("");
		dtpFecha.setValue(new Date());
		txtEmail.setText("");
		txtDireccion.setText("");
		cboServicio.setText("");
		servicioSeleccionado = null;
		cboEtnia.setText("");
		etniaSeleccionado = null;
		cboDiscapacidad.setText("");
		discapacidadSeleccionado = null;
		txtPorcentajeDiscapacidad.setText("0");
		cboInstruccion.setText("");
		instruccionSeleccionado = null;
		cboEstadoSalud.setText("");
		estadoSaludSeleccionado = null;
		cboTipoVivienda.setText("");
		viviendaSeleccionado = null;
		cboEstadoVivienda.setText("");
		estadoSaludSeleccionado = null;
		cboProvincia.setText("");
		cantones = new ArrayList<Canton>();
		cboCanton.setModel(new ListModelList(cantones));
		parroquias = new ArrayList<Parroquia>();
		cboParroquia.setModel(new ListModelList(parroquias));
		cboCanton.setText("");
		cboParroquia.setText("");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void limpiarRepresentante() {
		txtNombresRep.setText("");
		txtCedulaRep.setText("");
		txtTelefonoRep.setText("");
		cboGeneroRep.setText("");
		txtCelularRep.setText("");
		txtApellidosRep.setText("");
		txtEmailRep.setText("");
		txtDireccionRep.setText("");
		txtIngresoMensual.setText("");
		dtpFechaRep.setValue(new Date());
		cboProvinciaRep.setText("");
		cantones = new ArrayList<Canton>();
		cboCantonRep.setModel(new ListModelList(cantones));
		parroquias = new ArrayList<Parroquia>();
		cboParroquiaRep.setModel(new ListModelList(parroquias));
		cboCantonRep.setText("");
		cboParroquiaRep.setText("");
	}
	
	private void bloquearComponentes() {
		txtCedula.setDisabled(true);
		txtNombres.setDisabled(true);
		txtTelefono.setDisabled(true);
		cboGenero.setDisabled(true);
		txtCelular.setDisabled(true);
		txtApellidos.setDisabled(true);
		dtpFecha.setDisabled(true);
		txtEmail.setDisabled(true);
		txtDireccion.setDisabled(true);
		cboServicio.setDisabled(true);
		cboEtnia.setDisabled(true);
		cboDiscapacidad.setDisabled(true);
		txtPorcentajeDiscapacidad.setDisabled(true);
		cboInstruccion.setDisabled(true);
		cboEstadoSalud.setDisabled(true);
		cboTipoVivienda.setDisabled(true);
		cboEstadoVivienda.setDisabled(true);
		cboProvincia.setDisabled(true);
		cboCanton.setDisabled(true);
		cboParroquia.setDisabled(true);
		
		txtNombresRep.setDisabled(true);
		txtCedulaRep.setDisabled(true);
		txtTelefonoRep.setDisabled(true);
		cboGeneroRep.setDisabled(true);
		txtCelularRep.setDisabled(true);
		txtApellidosRep.setDisabled(true);
		txtEmailRep.setDisabled(true);
		txtDireccionRep.setDisabled(true);
		txtIngresoMensual.setDisabled(true);
		dtpFechaRep.setDisabled(true);
		cboProvinciaRep.setDisabled(true);
		cboCantonRep.setDisabled(true);
		cboParroquiaRep.setDisabled(true);
		
		btnRegistrar.setDisabled(true);
		btnNuevo.setDisabled(true);
	}
	
	//metodo para buscar representante
	@Listen("onOK=#txtCedulaRep;onBlur=#txtCedulaRep")
	public void buscarRepresentante(){
		System.out.println("entra buscar representante por cedula");
		if(txtCedula.getText().equals("")) {
			Clients.showNotification("Debe de seleccionar una persona a registrar");
			return;
		}
		List<Representante> listaRepresentante = representanteDAO.getRepresentanteByCedula(txtCedulaRep.getText());
		if(listaRepresentante.size() > 0) {
			System.out.println(listaRepresentante.size());
			representanteRecuperado = listaRepresentante.get(0);
			recuperarDatosRepresentante(representanteRecuperado);
		}else {
			representanteRecuperado = new Representante();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void recuperarDatosRepresentante(Representante representante) {
		txtNombresRep.setText(representante.getNombre());
		txtCedulaRep.setText(representante.getCedula());
		txtTelefonoRep.setText(representante.getTelefono());
		cboGeneroRep.setText(representante.getGenero());
		txtCelularRep.setText(representante.getCelular());
		txtApellidosRep.setText(representante.getApellido());
		txtEmailRep.setText(representante.getEmail());
		txtDireccionRep.setText(representante.getReferenciaDomicilio());
		txtIngresoMensual.setText(String.valueOf(representante.getIngresoMensual()));
		dtpFechaRep.setValue(representante.getFechaNacimiento());
		
		cboProvinciaRep.setText(representante.getParroquia().getCanton().getProvincia().getDescripcion());
		for(int i = 0; i < cboProvincia.getItems().size(); i++){
			Provincia p = (Provincia)cboProvinciaRep.getItems().get(i).getValue();
			if(p.getDescripcion().equals(representante.getParroquia().getCanton().getProvincia().getDescripcion()))
				cboProvinciaRep.setSelectedIndex(i);
		}
		
		cboCantonRep.setText("");
		cboParroquiaRep.setText("");
		
		cantones = cantonDAO.getCantones(representante.getParroquia().getCanton().getProvincia().getIdProvincia());
		cboCantonRep.setModel(new ListModelList(cantones));
		parroquias = new ArrayList<Parroquia>();
		cboParroquiaRep.setModel(new ListModelList(parroquias));
		
		cboCantonRep.setText(representante.getParroquia().getCanton().getDescripcion());
		
		cboParroquiaRep.setText("");
		parroquias = parroquiaDAO.getParroquias(representante.getParroquia().getCanton().getIdCanton());
		cboParroquiaRep.setModel(new ListModelList(parroquias));
		//cantonSeleccionado = null;
		cboParroquia.setText(representante.getParroquia().getDescripcion());
		
	}
	
	//metodo para buscar la persona al presionar la tecla enter en el casilledo de cedula. la busqueda se hace mediante la cedula
	@Listen("onOK=#txtCedula;onBlur=#txtCedula")
	public void buscar(){
		System.out.println("entra buscar persona por cedula");
		List<Persona> listaPersonas = personaDAO.getPersonaByCedula(txtCedula.getText());
		if(listaPersonas.size() > 0) {
			if(verificarMatricula(listaPersonas.get(0))) {//manda verdadero si existe 
				Clients.showNotification("Persona ya se encuentra matriculada en uno de los centros");
				txtCedula.setFocus(true);
				limpiarPersona();
				limpiarRepresentante();
				return;
			}
			System.out.println(listaPersonas.size());
			personaRecuperada = listaPersonas.get(0);
			recuperarDatos(personaRecuperada);
		}else {
			personaRecuperada = new Persona();
			limpiarPersona();
			limpiarRepresentante();
		}
	}
	
	private boolean verificarMatricula(Persona persona) {
		boolean bandera = false;
		List<Matricula> lista = matriculaDAO.getPersonaMatricula(txtCedula.getText());
		if(lista.size() > 0)
			bandera = true;
		return bandera;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void recuperarDatos(Persona persona) {
		txtCedula.setText(persona.getCedula());
		txtNombres.setText(persona.getNombre());
		txtTelefono.setText(persona.getTelefono());
		cboGenero.setText(persona.getGenero());
		txtCelular.setText(persona.getCelular());
		txtApellidos.setText(persona.getApellido());
		dtpFecha.setValue(persona.getFechaNacimiento());
		txtEmail.setText(persona.getEmail());
		txtDireccion.setText(persona.getReferenciaDomicilio());
		cboServicio.setText(persona.getServicio().getDescripcion());
		cboEtnia.setText(persona.getEtnia().getDescripcion());
		cboDiscapacidad.setText(persona.getDiscapacidad().getDescripcion());
		txtPorcentajeDiscapacidad.setText(String.valueOf(persona.getProcentajeDiscapacidad()));
		cboInstruccion.setText(persona.getInstruccion().getDescripcion());
		cboEstadoSalud.setText(persona.getSalud().getDescripcion());
		cboTipoVivienda.setText(persona.getVivienda().getDescripcion());
		cboEstadoVivienda.setText(persona.getEstadoVivienda().getDescripcion());
		
		
		cboProvincia.setText(persona.getParroquia().getCanton().getProvincia().getDescripcion());
		for(int i = 0; i < cboProvincia.getItems().size(); i++){
			Provincia p = (Provincia)cboProvincia.getItems().get(i).getValue();
			if(p.getDescripcion().equals(persona.getParroquia().getCanton().getProvincia().getDescripcion()))
				cboProvincia.setSelectedIndex(i);
		}
		
		cboCanton.setText("");
		cboParroquia.setText("");
		
		cantones = cantonDAO.getCantones(persona.getParroquia().getCanton().getProvincia().getIdProvincia());
		cboCanton.setModel(new ListModelList(cantones));
		parroquias = new ArrayList<Parroquia>();
		cboParroquia.setModel(new ListModelList(parroquias));
		
		cboCanton.setText(persona.getParroquia().getCanton().getDescripcion());
		
		cboParroquia.setText("");
		parroquias = parroquiaDAO.getParroquias(persona.getParroquia().getCanton().getIdCanton());
		cboParroquia.setModel(new ListModelList(parroquias));
		//cantonSeleccionado = null;

		cboParroquia.setText(persona.getParroquia().getDescripcion());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onChange = #cboProvincia")
	public void cargarCantones() {
		Provincia p = (Provincia)cboProvincia.getSelectedItem().getValue();
		if(p == null) {
			Clients.showNotification("Debe seleccionar una provincia!");
			return;
		}
		cboCanton.setText("");
		cboParroquia.setText("");
		
		cantones = cantonDAO.getCantones(p.getIdProvincia());
		cboCanton.setModel(new ListModelList(cantones));
		parroquias = new ArrayList<Parroquia>();
		cboParroquia.setModel(new ListModelList(parroquias));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onChange = #cboCanton")
	public void cargarParroquias() {
		Canton c = (Canton)cboCanton.getSelectedItem().getValue();
		if(c == null) {
			Clients.showNotification("Debe seleccionar un canton!");
			return;
		}
		cboParroquia.setText("");
		parroquias = parroquiaDAO.getParroquias(c.getIdCanton());
		cboParroquia.setModel(new ListModelList(parroquias));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onChange = #cboProvinciaRep")
	public void cargarCantonesRep() {
		Provincia p = (Provincia)cboProvinciaRep.getSelectedItem().getValue();
		if(p == null) {
			Clients.showNotification("Debe seleccionar una provincia!");
			return;
		}
		cboCantonRep.setText("");
		cboParroquiaRep.setText("");
		
		cantonesRep = cantonDAO.getCantones(p.getIdProvincia());
		cboCantonRep.setModel(new ListModelList(cantonesRep));
		parroquiasRep = new ArrayList<Parroquia>();
		cboParroquiaRep.setModel(new ListModelList(parroquiasRep));
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onChange = #cboCantonRep")
	public void cargarParroquiasRep() {
		Canton c = (Canton)cboCantonRep.getSelectedItem().getValue();
		if(c == null) {
			Clients.showNotification("Debe seleccionar un canton!");
			return;
		}
		cboParroquiaRep.setText("");
		parroquiasRep = parroquiaDAO.getParroquias(c.getIdCanton());
		cboParroquiaRep.setModel(new ListModelList(parroquiasRep));
	}
	
	private boolean ValidarEmail (String correo) {
		Pattern pat = null;
		Matcher mat = null;        
		pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
		mat = pat.matcher(correo);
		if (mat.find()) {
			return true;
		}else{
			return false;
		}        
	}
	
	private boolean validarDeCedula(String cedula) {
		boolean cedulaCorrecta = false;
		try {
			if (cedula.length() == 10) // ConstantesApp.LongitudCedula
			{
				int tercerDigito = Integer.parseInt(cedula.substring(2, 3));
				if (tercerDigito < 6) {
					// Coeficientes de validación cédula
					// El decimo digito se lo considera dígito verificador
					int[] coefValCedula = { 2, 1, 2, 1, 2, 1, 2, 1, 2 };
					int verificador = Integer.parseInt(cedula.substring(9,10));
					int suma = 0;
					int digito = 0;
					for (int i = 0; i < (cedula.length() - 1); i++) {
						digito = Integer.parseInt(cedula.substring(i, i + 1))* coefValCedula[i];
						suma += ((digito % 10) + (digito / 10));
					}

					if ((suma % 10 == 0) && (suma % 10 == verificador)) {
						cedulaCorrecta = true;
					}
					else if ((10 - (suma % 10)) == verificador) {
						cedulaCorrecta = true;
					} else {
						cedulaCorrecta = false;
					}
				} else {
					cedulaCorrecta = false;
				}
			} else {
				cedulaCorrecta = false;
			}
		} catch (NumberFormatException nfe) {
			cedulaCorrecta = false;
		} catch (Exception err) {
			System.out.println("Una excepcion ocurrio en el proceso de validacion");
			cedulaCorrecta = false;
		}
		if (!cedulaCorrecta) {
			System.out.println("La Cédula ingresada es Incorrecta");
		}
		return cedulaCorrecta;
	}
	
	public List<Provincia> getProvincias() {
		return provinciaDAO.getProvincias();
	}
	
	public List<Servicio> getServicios() {
		return servicioDAO.getServicios("");
	}
	
	public List<Etnia> getEtnias() {
		return etniaDAO.getEtnias("");
	}
	
	public List<Discapacidad> getDiscapacidades() {
		return discapacidadDAO.getDiscapacidades("");
	}
	
	public List<Instruccion> getInstrucciones() {
		return instruccionDAO.getInstrucciones("");
	}
	
	public List<Salud> getEstadosSalud() {
		return saludDAO.getEstadosSalud();
	}
	
	public List<Vivienda> getViviendas() {
		return viviendaDAO.getViviendas();
	}
	
	public List<EstadoVivienda> getEstadosVivienda() {
		return estadoViviendaDAO.getEstadosVivienda();
	}
		
	public List<Provincia> getProvinciasRep() {
		return provinciaDAO.getProvincias();
	}
	
	public List<Canton> getCantones() {
		return cantones;
	}

	public void setCantones(List<Canton> cantones) {
		this.cantones = cantones;
	}

	public List<Parroquia> getParroquias() {
		return parroquias;
	}

	public void setParroquias(List<Parroquia> parroquias) {
		this.parroquias = parroquias;
	}

	public Provincia getProvinciaSeleccionado() {
		return provinciaSeleccionado;
	}

	public void setProvinciaSeleccionado(Provincia provinciaSeleccionado) {
		this.provinciaSeleccionado = provinciaSeleccionado;
	}

	public Canton getCantonSeleccionado() {
		return cantonSeleccionado;
	}

	public void setCantonSeleccionado(Canton cantonSeleccionado) {
		this.cantonSeleccionado = cantonSeleccionado;
	}

	public Parroquia getParroquiaSeleccionado() {
		return parroquiaSeleccionado;
	}

	public void setParroquiaSeleccionado(Parroquia parroquiaSeleccionado) {
		this.parroquiaSeleccionado = parroquiaSeleccionado;
	}

	public Provincia getProvinciaSeleccionadoRep() {
		return provinciaSeleccionadoRep;
	}

	public void setProvinciaSeleccionadoRep(Provincia provinciaSeleccionadoRep) {
		this.provinciaSeleccionadoRep = provinciaSeleccionadoRep;
	}

	public Canton getCantonSeleccionadoRep() {
		return cantonSeleccionadoRep;
	}

	public void setCantonSeleccionadoRep(Canton cantonSeleccionadoRep) {
		this.cantonSeleccionadoRep = cantonSeleccionadoRep;
	}

	public Parroquia getParroquiaSeleccionadoRep() {
		return parroquiaSeleccionadoRep;
	}

	public void setParroquiaSeleccionadoRep(Parroquia parroquiaSeleccionadoRep) {
		this.parroquiaSeleccionadoRep = parroquiaSeleccionadoRep;
	}

	public List<Canton> getCantonesRep() {
		return cantonesRep;
	}

	public void setCantonesRep(List<Canton> cantonesRep) {
		this.cantonesRep = cantonesRep;
	}

	public List<Parroquia> getParroquiasRep() {
		return parroquiasRep;
	}

	public void setParroquiasRep(List<Parroquia> parroquiasRep) {
		this.parroquiasRep = parroquiasRep;
	}
	
	public Persona getPersonaRecuperada() {
		return personaRecuperada;
	}

	public void setPersonaRecuperada(Persona personaRecuperada) {
		this.personaRecuperada = personaRecuperada;
	}

	public Servicio getServicioSeleccionado() {
		return servicioSeleccionado;
	}

	public void setServicioSeleccionado(Servicio servicioSeleccionado) {
		this.servicioSeleccionado = servicioSeleccionado;
	}

	public Etnia getEtniaSeleccionado() {
		return etniaSeleccionado;
	}

	public void setEtniaSeleccionado(Etnia etniaSeleccionado) {
		this.etniaSeleccionado = etniaSeleccionado;
	}

	public Discapacidad getDiscapacidadSeleccionado() {
		return discapacidadSeleccionado;
	}

	public void setDiscapacidadSeleccionado(Discapacidad discapacidadSeleccionado) {
		this.discapacidadSeleccionado = discapacidadSeleccionado;
	}

	
	public Instruccion getInstruccionSeleccionado() {
		return instruccionSeleccionado;
	}

	public void setInstruccionSeleccionado(Instruccion instruccionSeleccionado) {
		this.instruccionSeleccionado = instruccionSeleccionado;
	}

	public Salud getEstadoSaludSeleccionado() {
		return estadoSaludSeleccionado;
	}

	public void setEstadoSaludSeleccionado(Salud estadoSaludSeleccionado) {
		this.estadoSaludSeleccionado = estadoSaludSeleccionado;
	}

	public Vivienda getViviendaSeleccionado() {
		return viviendaSeleccionado;
	}

	public void setViviendaSeleccionado(Vivienda viviendaSeleccionado) {
		this.viviendaSeleccionado = viviendaSeleccionado;
	}
	
	public EstadoVivienda getEstadoViviendaSeleccionado() {
		return estadoViviendaSeleccionado;
	}

	public void setEstadoViviendaSeleccionado(EstadoVivienda estadoViviendaSeleccionado) {
		this.estadoViviendaSeleccionado = estadoViviendaSeleccionado;
	}

	@SuppressWarnings("unused")
	private void showNotify(String msg, Component ref) {
        Clients.showNotification(msg, "info", ref, "end_center", 2000);
    }
}
