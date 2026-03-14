package com.umg.edu.consumer.model;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaccion {
	private String IdTransaccion;
	private double monto;
	private String moneda;
	private String cuentaOrigen;
	private String bancoDestino;
	private Detalle detalle;
	public String getIdTransaccion() {
		return IdTransaccion;
	}
	public void setIdTransaccion(String idTransaccion) {
		IdTransaccion = idTransaccion;
	}
	public double getMonto() {
		return monto;
	}
	public void setMonto(double monto) {
		this.monto = monto;
	}
	

}
