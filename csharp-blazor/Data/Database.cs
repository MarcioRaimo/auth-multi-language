using Microsoft.JSInterop;
using System.Text.Json;
using System.Text.Json.Nodes;

namespace csharp_blazor.Data
{
	public class Database
	{
		public static List<InputData> readFile()
		{
			string path = Path.Combine(Directory.GetCurrentDirectory(), "Data", "database.json");
			string file = File.ReadAllText(path);
			return JsonSerializer.Deserialize<List<InputData>>(file);
		}

		public static void writeFile(List<InputData> data, IJSRuntime js)
		{
			foreach (InputData input in data)
			{
				js.InvokeVoidAsync("console.log", "Meu pau de asa 2");
				js.InvokeVoidAsync("console.log", input.email);
				js.InvokeVoidAsync("console.log", input.password);
			}
			string path = Path.Combine(Directory.GetCurrentDirectory(), "Data", "database.json");
			string toWrite = JsonSerializer.Serialize(data);
			File.WriteAllText(path, toWrite);
		}
	}
}
