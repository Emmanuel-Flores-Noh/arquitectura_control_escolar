# **Caso a examinar**
Una universidad tiene una aplicación de control escolar donde un único componente realiza las siguientes funciones:
  - Muestra formularios.
  - Valida datos.
  - Consulta calificaciones.
  - Calcula promedios.
  - Accede directamente a la base de datos.
  - Se comunica con otros sistemas.
  - Genera reportes.
  - Gestiona mensajes de error.

Cuando se modifica la interfaz, también deben modificarse las reglas de negocio. Cuando cambia la conexión con otro sistema, varias partes de la aplicación dejan de funcionar.

# **1. Diagnóstico de la arquitectura actual**
  - **Responsabilidades mezcladas:** La interfaz, la lógica de negocio, las reglas, las consultas y el acceso a datos residen en un único componente.
  - **Acoplamiento alto:** Cuando cambia la conexión con otro sistema, varias partes de la aplicación dejan de funcionar.
  - **Cohesión baja:** El componente asume tareas de diversos tipos que no muestran una correlación “inmediata”, cómo mostrar formularios, calcular promedios, generar reportes y gestionar errores.
  - **Duplicación y rigidez lógica:** Cuando se modifica la interfaz (La UI), deben modificarse forzosamente las reglas de negocio en el mismo bloque.
  - **Ausencia de interfaces:** Dificultad para realizar pruebas unitarias o de integración debido a la falta de contratos.

# **2. Identificación de responsabilidades**
La arquitectura actual se nota que es un sistema monolítico, todo está centralizado en un mismo programa/hilo de ejecución, lo que dificulta su expansión y mantenimiento.

| **Responsabilidad** | **Componente actual** | **Componente propuesto** |
| :--- | :--- | :--- |
| Mostrar información | Monolito centralizado | Capa de Vista (patrón MVC) |
| Recibir acciones | Monolito centralizado | Controlador (patrón MVC) |
| Aplicar reglas | Monolito centralizado | Capa de Negocio / Servicios (Arquitectura por capas) |
| Acceder a datos | Monolito centralizado | Capa de Acceso a Datos (DAO) |
| Comunicar servicios | Monolito centralizado | Patrón Broker |
| Gestionar errores | Monolito centralizado | Módulo Centralizado de Excepciones y Logging |

# **3. Selección de patrones**
  - **Patrón Modelo-Vista-Controlador (MVC):** Para separar la interacción del usuario (Vista), el control de flujo (Controlador) y la lógica principal/datos (Modelo).
  - **Patrón Broker:** Para encapsular y desacoplar la comunicación con sistemas remotos y externos mediante servicios intermediarios.
  - **Arquitectura en Capas:** Para estructurar el sistema jerárquicamente, asegurando que cada capa dependa únicamente de la inferior inmediata.

# **4. Diseño de la nueva arquitectura**
  - **Componentes:** Vista (Interfaz gráfica para computadora hecha con JavaFX), Controlador (Gestión de peticiones), Modelo/Servicios (Lógica de negocio y cálculo de promedios), Capa de Persistencia (Acceso a base de datos), Broker de Servicios (Integración con sistemas externos).
  - **Responsabilidades:** Delimitación clara y modular de cada bloque funcional para evitar interferencias entre áreas del sistema.
  - **Interfaces:** Definición de contratos formales entre capas para un desarrollo independiente.
  - **Relaciones permitidas y dirección de dependencias:** Unidireccionales desde la capa superior hacia la inferior (la Vista conoce al Controlador, este al Modelo, y el Modelo a la Persistencia).
  - **Patrón aplicado y contratos:** Integración de MVC para la capa de presentación y Broker para servicios externos con contratos estrictos de comunicación.

# **5. Análisis de cambio**
  - **1. Crear una aplicación móvil:** La arquitectura MVC y la separación de la capa de servicios permiten añadir una nueva interfaz (móvil) reutilizando toda la lógica de negocio existente sin modificar el núcleo.
  - **2. Cambiar la base de datos:** Gracias a la capa de acceso a datos y el uso de patrones de persistencia, el cambio afecta únicamente al componente de datos, manteniendo intactas las reglas de negocio y las vistas.
  - **3. Consumir un nuevo servicio remoto:** El patrón Broker aísla la comunicación externa; integrar un nuevo servicio requiere únicamente implementar un nuevo adaptador o conector en el broker sin alterar el resto del sistema.
  - **4. Agregar una vista para personal directivo:** Al estar desacoplada la presentación, se puede construir un nuevo módulo de vistas consumiendo los mismos servicios y controladores existentes.
  - **5. Sustituir el proveedor de autenticación:** Se gestiona de forma modular a través de un servicio o componente de seguridad especializado, evitando modificaciones generalizadas en la aplicación.

# **6. Evaluación de atributos de calidad**
  - **Mantenibilidad:** Alta, ya que los cambios están acotados a módulos específicos.
  - **Modificabilidad:** Facilita modificaciones en vistas o bases de datos de forma independiente.
  - **Testabilidad:** Permite realizar pruebas unitarias y automatizadas por componente gracias a la separación de responsabilidades y el uso de interfaces.
  - **Interoperabilidad:** Mejorada sustancialmente mediante el uso del patrón Broker y contratos definidos para servicios externos.
  - **Rendimiento:** Optimizado al evitar bloqueos directos y permitir escalabilidad modular.
  - **Seguridad:** Centralizada en capas específicas de autenticación y control de acceso.

## Conclusión grupal
Como equipo en esta tarea hemos visto cómo las buenas prácticas, patrones de diseño y las decisiones arquitectónicas influyen en la calidad de una solución de software. Claro, el producto planteado por la actividad funciona, y el trabajo que realiza debe hacerlo bien; el problema surge al momento de escalar o darle mantenimiento al sistema (de ahí es donde se manifiestan la mayoría de problemas) debido a la deuda técnica. Cambiar lógicas de negocio, bases de datos o la interfaz gráfica es una tarea monumental y abismal debido al alto acoplamiento del sistema. Sin embargo, también pensamos —como hemos visto en clase— que a veces una arquitectura “simple” en cuanto a implementar patrones de diseño también es una solución válida dependiendo de la situación y el producto que se desee realizar. Desde luego, es responsabilidad del ingeniero de software detectar cuándo hace falta un sistema más robusto y justificar el porqué de aquellas decisiones.