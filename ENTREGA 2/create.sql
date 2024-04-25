create sequence sesion_seq start with 1 increment by 50;
create table sesion (id integer not null, id_cliente integer not null, id_entrenador integer not null, id_plan integer not null, presencial boolean, descripcion varchar(255), fin varchar(255), inicio varchar(255), trabajo_realizado varchar(255), datos_salud varchar(255) array, multimedia varchar(255) array, primary key (id));
