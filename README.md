API de Gestión de Gastos de Viaje
Esta API permite registrar y consultar los gastos de viaje realizados por empleados, ofreciendo funcionalidades CRUD, reportes mensuales y cálculo de IVA.

Relaciones de Tablas
EMPLOYEE: Tabla de empleados.

TRAVEL_EXPENSE: Tabla de gastos de viaje.

Relación: TRAVEL_EXPENSE.EMPLOYEE_ID → EMPLOYEE.ID

Comandos para Iniciar el Proyecto
Windows:
bash:
.\gradlew bootRun

Linux/macOS:
bash
./gradlew bootRun

Endpoints de la API
Puedes probar estos endpoints con herramientas como Postman o Insomnia.

1. ✅ Obtener el Resumen de Gastos
Método: GET

URL: /api/expenses/summary

Descripción: Devuelve un resumen de todos los gastos, agrupados por empleado y mes. Incluye IVA y quién asume el gasto.

📘 Ejemplo de Respuesta (200 OK):

json
{
  "grandTotal": 17959998.00,
  "employeeSummaries": [
    {
      "employeeName": "Adam",
      "monthlyExpenses": [
        {
          "month": "JANUARY",
          "subtotal": 3000000.00,
          "ivaAmount": 570000.00,
          "totalWithIva": 3570000.00,
          "suraAssumes": true
        },
        {
          "month": "FEBRUARY",
          "subtotal": 500000.00,
          "ivaAmount": 95000.00,
          "totalWithIva": 595000.00,
          "suraAssumes": false
        }
      ]
    }
  ]
}

2. 🆕 Crear un Nuevo Gasto (Simple)
Método: POST

URL: /api/expenses/simple

Descripción: Crea un gasto. Si el empleado no existe, se crea automáticamente. La fecha es la del servidor.

📥 Cuerpo de la petición:

json
{
  "employeeName": "Nuevo Empleado",
  "value": 150000
}
📘 Respuesta Exitosa (201 Created):

json
Copiar
Editar
{
  "id": 14,
  "employee": {
    "id": 8,
    "name": "Nuevo Empleado"
  },
  "expenseDate": "AAAA-MM-DD",
  "value": 150000
}

3. 🔍 Buscar un Gasto por ID
Método: GET

URL: /api/expenses/{id}

4. ✏️ Actualizar un Gasto
Método: PUT

URL: http://localhost:8080/api/expenses/5

📥 Cuerpo del JSON:

json
{
  "employeeName": "CRISTIAN",
  "expenseDate": "2025-07-02",
  "value": 250000
}



🛢️ Consola de la Base de Datos H2
Puedes acceder a la consola web de la base de datos H2 en:

🔗 http://localhost:8080/h2-console

Detalles de conexión:

URL JDBC: jdbc:h2:mem:testdb

Usuario: sa

Contraseña: (dejar en blanco)

🧪 Consultas Útiles en la Base de Datos
📋 Ver todos los registros
sql
SELECT * FROM EMPLOYEE;
SELECT * FROM TRAVEL_EXPENSE;
🧾 Ver los gastos con el nombre del empleado

sql
SELECT
    TE.ID,
    E.NAME,
    TE.EXPENSE_DATE,
    TE.EXPENSE_VALUE
FROM
    TRAVEL_EXPENSE TE
JOIN
    EMPLOYEE E ON TE.EMPLOYEE_ID = E.ID;
    
👤 Ver los gastos de un empleado específico
sql
SELECT
    E.NAME,
    TE.EXPENSE_DATE,
    TE.EXPENSE_VALUE
FROM
    TRAVEL_EXPENSE TE
JOIN
    EMPLOYEE E ON TE.EMPLOYEE_ID = E.ID
WHERE
    E.NAME = 'Adam';

    
📑 Documentación Interactiva (Swagger)
http://localhost:8080/swagger-ui/index.html

