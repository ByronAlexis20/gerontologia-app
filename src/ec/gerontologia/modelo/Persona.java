package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the persona database table.
 * 
 */
@Entity
@Table(name="persona")
@NamedQueries({
	@NamedQuery(name="Persona.buscarPorCedula", query="SELECT p FROM Persona p where p.estado = 'A' and p.cedula = :cedula")
})
public class Persona implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_persona")
	private Integer idPersona;

	@Lob
	private String apellido;

	@Lob
	private String cedula;

	@Lob
	private String celular;

	@Lob
	private String email;

	private String estado;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_nacimiento")
	private Date fechaNacimiento;

	@Lob
	private String genero;

	@Lob
	private String nombre;

	@Column(name="procentaje_discapacidad")
	private int procentajeDiscapacidad;

	@Lob
	@Column(name="referencia_domicilio")
	private String referenciaDomicilio;

	@Lob
	private String telefono;

	//bi-directional many-to-one association to Matricula
	@OneToMany(mappedBy="persona", cascade = CascadeType.ALL)
	private List<Matricula> matriculas;

	//bi-directional many-to-one association to Discapacidad
	@ManyToOne
	@JoinColumn(name="id_discapacidad")
	private Discapacidad discapacidad;

	//bi-directional many-to-one association to Documento
	@ManyToOne
	@JoinColumn(name="id_documento")
	private Documento documento;

	//bi-directional many-to-one association to EstadoVivienda
	@ManyToOne
	@JoinColumn(name="id_estado_vivienda")
	private EstadoVivienda estadoVivienda;

	//bi-directional many-to-one association to Etnia
	@ManyToOne
	@JoinColumn(name="id_etnia")
	private Etnia etnia;

	//bi-directional many-to-one association to Instruccion
	@ManyToOne
	@JoinColumn(name="id_instruccion")
	private Instruccion instruccion;

	//bi-directional many-to-one association to Parroquia
	@ManyToOne
	@JoinColumn(name="id_parroquia")
	private Parroquia parroquia;

	//bi-directional many-to-one association to Salud
	@ManyToOne
	@JoinColumn(name="id_salud")
	private Salud salud;

	//bi-directional many-to-one association to Servicio
	@ManyToOne
	@JoinColumn(name="id_servicio")
	private Servicio servicio;

	//bi-directional many-to-one association to Vivienda
	@ManyToOne
	@JoinColumn(name="id_vivienda")
	private Vivienda vivienda;

	public Persona() {
	}

	public Integer getIdPersona() {
		return this.idPersona;
	}

	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getCelular() {
		return this.celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Date getFechaNacimiento() {
		return this.fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getGenero() {
		return this.genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getProcentajeDiscapacidad() {
		return this.procentajeDiscapacidad;
	}

	public void setProcentajeDiscapacidad(int procentajeDiscapacidad) {
		this.procentajeDiscapacidad = procentajeDiscapacidad;
	}

	public String getReferenciaDomicilio() {
		return this.referenciaDomicilio;
	}

	public void setReferenciaDomicilio(String referenciaDomicilio) {
		this.referenciaDomicilio = referenciaDomicilio;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public List<Matricula> getMatriculas() {
		return this.matriculas;
	}

	public void setMatriculas(List<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	public Matricula addMatricula(Matricula matricula) {
		getMatriculas().add(matricula);
		matricula.setPersona(this);

		return matricula;
	}

	public Matricula removeMatricula(Matricula matricula) {
		getMatriculas().remove(matricula);
		matricula.setPersona(null);

		return matricula;
	}

	public Discapacidad getDiscapacidad() {
		return this.discapacidad;
	}

	public void setDiscapacidad(Discapacidad discapacidad) {
		this.discapacidad = discapacidad;
	}

	public Documento getDocumento() {
		return this.documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public EstadoVivienda getEstadoVivienda() {
		return this.estadoVivienda;
	}

	public void setEstadoVivienda(EstadoVivienda estadoVivienda) {
		this.estadoVivienda = estadoVivienda;
	}

	public Etnia getEtnia() {
		return this.etnia;
	}

	public void setEtnia(Etnia etnia) {
		this.etnia = etnia;
	}

	public Instruccion getInstruccion() {
		return this.instruccion;
	}

	public void setInstruccion(Instruccion instruccion) {
		this.instruccion = instruccion;
	}

	public Parroquia getParroquia() {
		return this.parroquia;
	}

	public void setParroquia(Parroquia parroquia) {
		this.parroquia = parroquia;
	}

	public Salud getSalud() {
		return this.salud;
	}

	public void setSalud(Salud salud) {
		this.salud = salud;
	}

	public Servicio getServicio() {
		return this.servicio;
	}

	public void setServicio(Servicio servicio) {
		this.servicio = servicio;
	}

	public Vivienda getVivienda() {
		return this.vivienda;
	}

	public void setVivienda(Vivienda vivienda) {
		this.vivienda = vivienda;
	}

}