package ec.gerontologia.modelo;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the representante database table.
 * 
 */
@Entity
@Table(name="representante")
@NamedQueries({
	@NamedQuery(name="Representante.buscarPorCedula", query="SELECT r FROM Representante r where r.estado = 'A' and r.cedula = :cedula")
})
public class Representante implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_representante")
	private Integer idRepresentante;

	@Lob
	private String apellido;

	@Lob
	private String cedula;

	@Lob
	private String celular;

	private String estado;
	
	private String genero;

	@Column(name="ingreso_mensual")
	private double ingresoMensual;

	@Temporal(TemporalType.DATE)
	@Column(name="fecha_nacimiento")
	private Date fechaNacimiento;
	
	@Lob
	private String nombre;
	
	private String email;

	@Column(name="procentaje_discapacidad")
	private int procentajeDiscapacidad;

	@Lob
	@Column(name="referencia_domicilio")
	private String referenciaDomicilio;

	@Lob
	private String telefono;

	//bi-directional many-to-one association to Matricula
	@OneToMany(mappedBy="representante", cascade = CascadeType.ALL)
	private List<Matricula> matriculas;

	//bi-directional many-to-one association to Discapacidad
	@ManyToOne
	@JoinColumn(name="id_discapacidad")
	private Discapacidad discapacidad;

	//bi-directional many-to-one association to Instruccion
	@ManyToOne
	@JoinColumn(name="id_instruccion")
	private Instruccion instruccion;

	//bi-directional many-to-one association to Parroquia
	@ManyToOne
	@JoinColumn(name="id_parroquia")
	private Parroquia parroquia;

	public Representante() {
	}

	public Integer getIdRepresentante() {
		return this.idRepresentante;
	}

	public void setIdRepresentante(Integer idRepresentante) {
		this.idRepresentante = idRepresentante;
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

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public double getIngresoMensual() {
		return this.ingresoMensual;
	}

	public void setIngresoMensual(double ingresoMensual) {
		this.ingresoMensual = ingresoMensual;
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
		matricula.setRepresentante(this);

		return matricula;
	}

	public Matricula removeMatricula(Matricula matricula) {
		getMatriculas().remove(matricula);
		matricula.setRepresentante(null);

		return matricula;
	}

	public Discapacidad getDiscapacidad() {
		return this.discapacidad;
	}

	public void setDiscapacidad(Discapacidad discapacidad) {
		this.discapacidad = discapacidad;
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

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

}