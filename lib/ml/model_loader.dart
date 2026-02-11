import 'dart:convert';
import 'package:flutter/services.dart' show rootBundle;

class SmishModel {
  final Map<String, int> vocab;   // word → index
  final List<double> coefficients;
  final double intercept;
  final double threshold;

  SmishModel({
    required this.vocab,
    required this.coefficients,
    required this.intercept,
    required this.threshold,
  });

  static Future<SmishModel> load() async {
    final jsonString = await rootBundle.loadString("assets/ml/model.json");
    final data = jsonDecode(jsonString);

    return SmishModel(
      vocab: Map<String, int>.from(data["vocab"]),
      coefficients: List<double>.from((data["coefficients"] ?? []).map((x) => (x as num).toDouble())),
      intercept: (data["intercept"] as num).toDouble(),
      threshold: (data["threshold"] as num?)?.toDouble() ?? 0.5,
    );
  }
}
